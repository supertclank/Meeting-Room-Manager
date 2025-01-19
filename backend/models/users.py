from sqlalchemy import Column, Integer, String, DateTime, Enum
from sqlalchemy.sql import func
from sqlalchemy.orm import relationship
from .base import Base
import enum
from datetime import datetime

# Enum
class UserType(str, enum.Enum):
    User = "User"
    Admin = "Admin"

# User Table
class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    first_name = Column(String(255), nullable=False)
    last_name = Column(String(255), nullable=False)
    email = Column(String(255), nullable=False, unique=True)
    password = Column(String(255), nullable=False)
    created_at = Column(DateTime, default=datetime.utcnow)
    user_type = Column(Enum(UserType), default=UserType.User)

    # Relationships
    bookings = relationship("Booking", back_populates="user", lazy='dynamic')
    notifications = relationship("Notification", back_populates="user", lazy='dynamic')