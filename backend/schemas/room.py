from pydantic import BaseModel
from backend.models.room import Room

#Room Schemas
class RoomCreate(BaseModel):
    name: str
    capacity: int
    amenities: str | None
    availability: bool
    
class RoomRead(BaseModel):
    id: int

    class Config:
        orm_mode = True
