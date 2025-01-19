from models.users import User
from datetime import datetime, timedelta
from fastapi.security import OAuth2PasswordBearer
from passlib.context import CryptContext
from sqlalchemy.orm import Session
from jose import jwt
import logging

# Initialize the logger
logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

# JWT Token settings and password hashing context
class TokenManager:
    
    SECRET_KEY = "Test"
    ALGORITHM = "HS256"
    ACCESS_TOKEN_EXPIRE_MINUTES = 30
    
    # OAuth2 scheme
    oauth2_scheme = OAuth2PasswordBearer(tokenUrl="login")
    # Password hashing context
    pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

    # Password hashing and verification methods
    @classmethod
    def verify_password(cls, plain_password, hashed_password):
        return cls.pwd_context.verify(plain_password, hashed_password)

    # Authenticate user method
    @classmethod
    def authenticate_user(cls, db: Session, email: str, password: str):
        user = db.query(User).filter(User.email == email).first()
        logger.info(f"Retrieved user: {user}")  # Log the retrieved user object

        if user:
            is_valid_password = cls.verify_password(password, user.password)
            logger.info(f"Password verification result: {is_valid_password}")  # Log the password verification result
            
            if is_valid_password:
                return user
            
        return None

    # Create access token method
    @classmethod
    def create_access_token(cls, data: dict, expires_delta: timedelta = None) -> str:
        to_encode = data.copy()
        
        # Set expiration time
        expire = datetime.utcnow() + (expires_delta or timedelta(minutes=cls.ACCESS_TOKEN_EXPIRE_MINUTES))
        to_encode.update({"exp": expire})
        
        if 'id' in data:
            to_encode["id"] = data['id']
        else:
            raise ValueError("User ID must be provided in the token payload")

        # Encode the JWT
        encoded_jwt = jwt.encode(to_encode, cls.SECRET_KEY, algorithm=cls.ALGORITHM)
        return encoded_jwt