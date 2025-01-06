from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
import logging

from backend.schemas.users import UserCreate, UserRead
from backend.crud.users import create_user, get_user_by_email, get_user_by_id, delete_user
from backend.utils.database import get_db

# Initialize the logger
logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

router = APIRouter()

# Endpoint to read all users
@router.get("/")
async def read_users():
    logger.info("Fetching all users")
    return [{"username": "user1"}, {"username": "user2"}]

# Endpoint to create a new user
@router.post("/users/", response_model=UserRead)
def create_user(user: UserCreate, db: Session = Depends(get_db)):
    logger.info(f"Creating a new user with email: {user.email}")
    db_user = create_user(db, user)
    logger.info(f"User created with ID: {db_user.id}")
    return db_user

# Endpoint to read a user by ID
@router.get("/users/{user_id}", response_model=UserRead)
def read_user(user_id: int, db: Session = Depends(get_db)):
    logger.info(f"Fetching user with ID: {user_id}")
    db_user = get_user_by_id(db, user_id)
    if db_user is None:
        logger.warning(f"User with ID: {user_id} not found")
        raise HTTPException(status_code=404, detail="User not found")
    logger.info(f"User with ID: {user_id} found")
    return db_user

# Endpoint to read a user by email
@router.get("/users/", response_model=UserRead)
def read_user(email: str, db: Session = Depends(get_db)):
    logger.info(f"Fetching user with email: {email}")
    db_user = get_user_by_email(db, email)
    if db_user is None:
        logger.warning(f"User with email: {email} not found")
        raise HTTPException(status_code=404, detail="User not found")
    logger.info(f"User with email: {email} found")
    return db_user

#delete a user by ID
@router.delete("/users/{user_id}", response_model=UserRead)
def delete_user_by_id(user_id: int, db: Session = Depends(get_db)):
    logger.info(f"Deleting user with ID: {user_id}")
    db_user = get_user_by_id(db, user_id)
    if db_user is None:
        logger.warning(f"User with ID: {user_id} not found")
        raise HTTPException(status_code=404, detail="User not found")
    return delete_user(db, user_id)