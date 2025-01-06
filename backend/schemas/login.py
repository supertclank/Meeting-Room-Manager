from pydantic import BaseModel

# Login schemas
class LoginRequest(BaseModel):
    email: str
    password: str

class TokenResponse(BaseModel):
    id: int
    access_token: str
    token_type: str = "bearer"
    email: str