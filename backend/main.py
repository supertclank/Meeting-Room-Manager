# FastAPI imports
from fastapi import FastAPI, Depends, HTTPException, status, Body
from fastapi.middleware.cors import CORSMiddleware
from fastapi.security import OAuth2PasswordRequestForm, OAuth2PasswordBearer
from fastapi.encoders import jsonable_encoder
import uvicorn
import logging

from router.users import router as users_router
from router.bookings import router as bookings_router
from router.rooms import router as rooms_router
from router.login import router as login_router
from router.notification import router as notification_router

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Allow all origins; change to specific URLs for production
    allow_credentials=True,
    allow_methods=["*"],  # Allow all HTTP methods
    allow_headers=["*"]   # Allow all headers
)

# OAuth2 scheme
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="login")

# Password hashing context
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

def authenticate_user(db: Session, username: str, password: str):
    user = db.query(User).filter(User.username == username).first()
    logger.info(f"Retrieved user: {user}")  # Log the retrieved user object

    if user:
        is_valid_password = verify_password(password, user.password)
        logger.info(f"Password verification result: {is_valid_password}")  # Log the password verification result
        
        if is_valid_password:
            return user
            
    return None

def create_access_token(data: dict, expires_delta: timedelta = None) -> str:
    to_encode = data.copy()
    
    # Set expiration time
    expire = datetime.utcnow() + (expires_delta or timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES))
    to_encode.update({"exp": expire})
    
    if 'id' in data:
        to_encode["id"] = data['id']
    else:
        raise ValueError("User ID must be provided in the token payload")

    # Encode the JWT
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

#routers for endpoints

app.include_router(users_router, prefix="/users", tags=["users"])

app.include_router(bookings_router, prefix="/bookings", tags=["bookings"])

app.include_router(rooms_router, prefix="/rooms", tags=["rooms"])

app.include_router(login_router, prefix="/login", tags=["login"])

app.include_router(notification_router, prefix="/notifications", tags=["notifications"])

# Entry point to run the server
if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8000)