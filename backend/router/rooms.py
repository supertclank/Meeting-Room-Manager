from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
import logging

from backend.schemas.user import UserCreate, UserRead
from backend.crud.users import create_user, get_user_by_email, get_user_by_id
from database import get_db

# Initialize the logger
logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

router = APIRouter()