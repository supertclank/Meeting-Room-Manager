from sqlalchemy.orm import Session
from models.user import User
from schemas.user import UserCreate

def create_user(db: Session, user: UserCreate):
    db_user = User(email=user.email, hashed_password=user.hashed_password)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

def get_user_by_email(db: Session, email: str):
    return db.query(User).filter(User.email == email).first()

def get_user_by_id(db: Session, user_id: int):
    return db.query(User).filter(User.id == user_id).first()