from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
import logging

from schemas.booking import BookingCreate, BookingRead, BookingStatus
from schemas.users import UserRead, UserRole
from crud.booking import create_booking, update_booking, get_booking_by_id, delete_booking
from utils.dependencies import get_current_active_user, get_current_active_admin
from utils.database import get_db

# Initialize the logger
logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

router = APIRouter()

#create a new booking
@router.post("/", response_model=BookingRead)
def create_new_booking(
    booking: BookingCreate,
    db: Session = Depends(get_db),
    current_user: UserRead = Depends(get_current_active_user)
):
    return create_booking(db, booking, current_user.id)

#read a booking by ID
@router.get("/{booking_id}", response_model=BookingRead)
def read_booking_by_id(
    booking_id: int,
    db: Session = Depends(get_db),
    current_user: UserRead = Depends(get_current_active_user)
):
    booking = get_booking_by_id(db, booking_id)
    if booking is None:
        logger.warning(f"Booking with ID: {booking_id} not found")
        raise HTTPException(status_code=404, detail="Booking not found")
    if booking.user_id != current_user.id and current_user.role != UserRole.admin:
        logger.warning(f"User {current_user.id} is not authorized to view booking {booking_id}")
        raise HTTPException(status_code=403, detail="Not authorized to view this booking")
    return booking

#update a booking by ID
@router.put("/{booking_id}", response_model=BookingRead)
def update_booking_by_id(
    booking_id: int,
    booking: BookingStatus,
    db: Session = Depends(get_db),
    current_user: UserRead = Depends(get_current_active_user)
):
    existing_booking = get_booking_by_id(db, booking_id)
    if existing_booking is None:
        logger.warning(f"Booking with ID: {booking_id} not found")
        raise HTTPException(status_code=404, detail="Booking not found")
    if existing_booking.user_id != current_user.id and current_user.role != UserRole.admin:
        logger.warning(f"User {current_user.id} is not authorized to update booking {booking_id}")
        raise HTTPException(status_code=403, detail="Not authorized to update this booking")
    return update_booking(db, booking_id, booking)

#delete a booking by ID
@router.delete("/{booking_id}", response_model=BookingRead)
def delete_booking_by_id(
    booking_id: int,
    db: Session = Depends(get_db),
    current_user: UserRead = Depends(get_current_active_user)
):
    booking = get_booking_by_id(db, booking_id)
    if booking is None:
        logger.warning(f"Booking with ID: {booking_id} not found")
        raise HTTPException(status_code=404, detail="Booking not found")
    if booking.user_id != current_user.id and current_user.role != UserRole.admin:
        logger.warning(f"User {current_user.id} is not authorized to delete booking {booking_id}")
        raise HTTPException(status_code=403, detail="Not authorized to delete this booking")
    return delete_booking(db, booking_id)