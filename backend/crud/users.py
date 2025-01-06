from sqlalchemy.orm import Session
from backend.models.users import User
from backend.schemas.users import UserCreate

# Create a new user
def create_user(db: Session, user: UserCreate):
    db_user = User(email=user.email, hashed_password=user.hashed_password)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

# Read a user by id
def get_user_by_id(db: Session, user_id: int):
    return db.query(User).filter(User.id == user_id).first()

# update a user by id
def update_user(db: Session, user_id: int, user: UserCreate):
    db_user = get_user_by_id(db, user_id)
    db_user.email = user.email
    db_user.hashed_password = user.hashed_password
    db.commit()
    db.refresh(db_user)
    return db_user

# delete a user by id
def delete_user(db: Session, user_id: int):
    db_user = get_user_by_id(db, user_id)
    db.delete(db_user)
    db.commit()
    return db_user

def get_user_by_email(db: Session, email: str):
    return db.query(User).filter(User.email == email).first()