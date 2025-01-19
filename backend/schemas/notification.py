from pydantic import BaseModel
from datetime import datetime
from schemas.users import UserRead

#Notification Schemas
class NotificationBase(BaseModel):
    message: str
    sent_at: datetime

class NotificationCreate(NotificationBase):
    user_id: int
    booking_id: int | None

class NotificationRead(NotificationBase):
    id: int
    user: UserRead

    class Config:
        orm_mode = True