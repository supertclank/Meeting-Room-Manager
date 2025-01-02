import logging
from fastapi import APIRouter, Depends, HTTPException, status, Body
from fastapi.security import OAuth2PasswordBearer
from sqlalchemy.orm import Session
from jose import JWTError, jwt
from passlib.context import CryptContext
from datetime import timedelta

from backend.schemas.user import UserCreate, UserRead
from backend.schemas.login import LoginRequest, TokenResponse
from backend.crud.users import create_user, get_user_by_email, get_user_by_id
from database import get_db

# Initialize the logger
logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

router = APIRouter()

@app.post("/login/", response_model=TokenResponse)
async def login(
    login_request: LoginRequest = Body(...),
    db: Session = Depends(get_db)
):
    user = authenticate_user(db, login_request.email, login_request.password)

    if not user:
        logger.info(f"Failed login attempt for username: {login_request.username}")
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )

    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": user.username, "id": user.id},
        expires_delta=access_token_expires
    )

    logger.info(f"User {user.username} logged in successfully.")
    
    return TokenResponse(
        access_token=access_token,
        token_type="bearer",
        id=user.id,
        username=user.username,
        email=user.email
    )