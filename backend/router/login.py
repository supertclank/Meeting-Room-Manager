import logging
from fastapi import APIRouter, Depends, HTTPException, status, Body
from sqlalchemy.orm import Session
from datetime import timedelta

from backend.schemas.login import LoginRequest, TokenResponse
from backend.utils.database import get_db
from backend.utils.token import TokenManager

# Initialize the logger
logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

router = APIRouter()

#login
@router.post("/login/", response_model=TokenResponse)
async def login(
    login_request: LoginRequest = Body(...),
    db: Session = Depends(get_db)
):
    user = TokenManager.authenticate_user(db, login_request.email, login_request.password)

    if not user:
        logger.info(f"Failed login attempt for email: {login_request.email}")
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect email or password",
            headers={"WWW-Authenticate": "Bearer"},
        )

    access_token_expires = timedelta(minutes=TokenManager.ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = TokenManager.create_access_token(
        data={"sub": user.email, "id": user.id},
        expires_delta=access_token_expires
    )

    logger.info(f"User {user.email} logged in successfully.")
    
    return TokenResponse(
        access_token=access_token,
        token_type="bearer",
        id=user.id,
        username=user.username,
        email=user.email
    )