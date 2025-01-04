from pydantic import BaseModel
from datetime import datetime, date
from backend.models.booking import BookingStatus
from backend.schemas.users import UserRead
from backend.schemas.room import RoomRead

#Booking Schemas
class BookingBase(BaseModel):
    start_time: datetime
    end_time: datetime
    status: BookingStatus

class BookingCreate(BookingBase):
    user_id: int
    room_id: int
    booking_type_id: int
    generated_at: date

class BookingRead(BookingBase):
    id: int
    user: UserRead
    room: RoomRead

    class Config:
        orm_mode = True