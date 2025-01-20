from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer
from sqlalchemy.orm import Session
from jose import JWTError, jwt
from schemas.users import UserRole, UserRead
from crud.users import get_user_by_id
from utils.database import get_db
from models.users import UserType
import logging

# Initialize the logger
logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

SECRET_KEY = "Test"
ALGORITHM = "HS256"
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="login")

def get_user_id_from_token(token: str = Depends(oauth2_scheme)):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        user_id: str = payload.get("sub")
        if user_id is None:
            logger.error("User ID not found in token payload")
            raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid token")
        logger.info(f"Extracted user ID from token: {user_id}")
        return int(user_id)
    except JWTError as e:
        logger.error(f"JWTError: {e}")
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid token")

def get_current_user(db: Session = Depends(get_db), token: str = Depends(oauth2_scheme)) -> UserRead:
    user_id = get_user_id_from_token(token)
    user = get_user_by_id(db, user_id)
    if user is None:
        logger.error(f"No user found for ID: {user_id}")
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid credentials")
    logger.info(f"Authenticated user: {user.email}")
    return user

def get_current_active_user(current_user: UserRead = Depends(get_current_user)) -> UserRead:
    if current_user.user_type == UserType.User:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Inactive user")
    return current_user


def get_current_active_admin(current_user: UserRead = Depends(get_current_active_user)) -> UserRead:
    if current_user.user_type == UserRole.Admin:
        return current_user
    raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Not enough permissions")
