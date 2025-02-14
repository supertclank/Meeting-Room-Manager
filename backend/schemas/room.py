from pydantic import BaseModel

#Room Schemas
class RoomCreate(BaseModel):
    name: str
    capacity: int
    amenities: str | None
    availability: bool
    
class RoomRead(BaseModel):
    id: int
    name: str
    capacity: int
    amenities: str | None
    availability: bool

    class Config:
        orm_mode = True
