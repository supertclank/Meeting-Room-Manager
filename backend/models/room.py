from sqlalchemy import Column, Integer, String, Text, Boolean
from sqlalchemy.orm import relationship
from .base import Base

# Room Table
class Room(Base):
    __tablename__ = "rooms"

    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    name = Column(String(100), nullable=False)
    capacity = Column(Integer, nullable=False)
    amenities = Column(Text, nullable=True)
    availability = Column(Boolean, nullable=True)

    bookings = relationship("Booking", back_populates="room")