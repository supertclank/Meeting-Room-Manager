from pydantic import BaseModel
from datetime import date
import enum

#User Schemas
class UserBase(BaseModel):
    name: str
    email: str

class UserCreate(UserBase):
    password: str
    user_type_id: int
    generated_at: date

class UserRead(UserBase):
    id: int

    class Config:
        orm_mode = True
        
class UserRole(str, enum.Enum):
    User = "User"
    Admin = "Admin"