from sqlalchemy.orm import Session
from models.users import User
from schemas.users import UserCreate
import logging

logger = logging.getLogger(__name__)

# Create a new user
def create_user(db: Session, user: UserCreate):
    db_user = User(
        first_name=user.first_name,
        last_name=user.last_name,
        email=user.email,
        password=user.hashed_password,  # Ensure password is hashed before saving
        user_type=user.user_type
    )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

# Read a user by id
def get_user_by_id(db: Session, user_id: int):
    logger.info(f"Querying for user with ID: {user_id}")
    user = db.query(User).filter(User.id == user_id).first()
    if user:
        logger.info(f"User found: {user.email}")
    else:
        logger.warning(f"No user found with ID: {user_id}")
    return user

# Update a user by id
def update_user(db: Session, user_id: int, user: UserCreate):
    db_user = get_user_by_id(db, user_id)
    if db_user:
        db_user.first_name = user.first_name
        db_user.last_name = user.last_name
        db_user.email = user.email
        db_user.password = user.hashed_password
        db_user.user_type = user.user_type
        db.commit()
        db.refresh(db_user)
    return db_user

# Delete a user by id
def delete_user(db: Session, user_id: int):
    db_user = get_user_by_id(db, user_id)
    if db_user:
        db.delete(db_user)
        db.commit()
    return db_user

# Read a user by email
def get_user_by_email(db: Session, email: str):
    return db.query(User).filter(User.email == email).first()