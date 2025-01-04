from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer
from sqlalchemy.orm import Session
from jose import JWTError, jwt
from backend.schemas.users import UserRole, UserRead
from backend.crud.users import get_user_by_id
from database import get_db

# Define the secret key and algorithm used for JWT
SECRET_KEY = "your_secret_key"
ALGORITHM = "HS256"

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

# Dependency to get the current user from the current token
def get_user_id_from_token(token: str = Depends(oauth2_scheme)):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        user_id: str = payload.get("sub")
        if user_id is None:
            raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid token")
        return user_id
    except JWTError:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid token")

# Dependency to get the current user from the database
def get_current_user(db: Session = Depends(get_db), token: str = Depends(oauth2_scheme)) -> UserRead:
    user_id = get_user_id_from_token(token)
    user = get_user_by_id(db, user_id)
    if user is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid credentials")
    return user

# Dependency to get the current active user
def get_current_active_user(current_user: UserRead = Depends(get_current_user)) -> UserRead:
    if current_user.is_active:
        return current_user
    raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Inactive user")

# Dependency to get the current active admin
def get_current_active_admin(current_user: UserRead = Depends(get_current_active_user)) -> UserRead:
    if current_user.role == UserRole.admin:
        return current_user
    raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Not enough permissions")