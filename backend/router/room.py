from typing import List
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
import logging

from schemas.room import RoomCreate, RoomRead
from crud.room import create_room, get_room_by_id, delete_room, get_rooms
from utils.dependencies import get_current_active_admin
from utils.database import get_db

# Initialize the logger
logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

router = APIRouter()

@router.post("/", response_model=RoomRead, dependencies=[Depends(get_current_active_admin)])
def create_new_room(
    room: RoomCreate,
    db: Session = Depends(get_db)
):
    return create_room(db, room)

@router.get("/{room_id}", response_model=RoomRead, dependencies=[Depends(get_current_active_admin)])
def read_room_by_id(
    room_id: int,
    db: Session = Depends(get_db)
):
    room = get_room_by_id(db, room_id)
    if room is None:
        logger.warning(f"Room with ID: {room_id} not found")
        raise HTTPException(status_code=404, detail="Room not found")
    return room

@router.delete("/{room_id}", response_model=RoomRead, dependencies=[Depends(get_current_active_admin)])
def delete_room_by_id(room_id: int, db: Session = Depends(get_db)):
    logger.info(f"Deleting room with ID: {room_id}")
    room = get_room_by_id(db, room_id)
    if room is None:
        logger.warning(f"Room with ID: {room_id} not found")
        raise HTTPException(status_code=404, detail="Room not found")
    return delete_room(db, room_id)

@router.get("/", response_model=List[RoomRead], dependencies=[Depends(get_current_active_admin)])
def read_rooms(
    db: Session = Depends(get_db)
):
    rooms = get_rooms(db)
    if not rooms:
        logger.warning("No rooms found")
        raise HTTPException(status_code=404, detail="No rooms found")
    return rooms
