from sqlalchemy.orm import Session
from backend.models.booking import Booking
from backend.schemas.booking import BookingCreate

# Create a new booking
def create_booking(db: Session, booking: BookingCreate):
    db_booking = Booking(room_id=booking.room_id, user_id=booking.user_id, start_date=booking.start_date, end_date=booking.end_date)
    db.add(db_booking)
    db.commit()
    db.refresh(db_booking)
    return db_booking

# Read a booking by ID
def get_booking_by_id(db: Session, booking_id: int):
    return db.query(Booking).filter(Booking.id == booking_id).first()

# Update a booking by ID
def update_booking(db: Session, booking_id: int, booking: BookingCreate):
    db_booking = get_booking_by_id(db, booking_id)
    db_booking.room_id = booking.room_id
    db_booking.user_id = booking.user_id
    db_booking.start_date = booking.start_date
    db_booking.end_date = booking.end_date
    db.commit()
    db.refresh(db_booking)
    return db_booking

# Delete a booking by ID
def delete_booking(db: Session, booking_id: int):
    db_booking = get_booking_by_id(db, booking_id)
    db.delete(db_booking)
    db.commit()
    return db_booking