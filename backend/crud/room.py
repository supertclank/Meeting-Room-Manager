from sqlalchemy.orm import Session
from models.room import Room
from schemas.room import RoomCreate

# Create a new room
def create_room(db: Session, room: RoomCreate):
    db_room = Room(name=room.name, capacity=room.capacity, amenities=room.amenities, availability=room.availability)
    db.add(db_room)
    db.commit()
    db.refresh(db_room)
    return db_room

# Read a room by ID
def get_room_by_id(db: Session, room_id: int):
    return db.query(Room).filter(Room.id == room_id).first()

# Update a room by ID
def update_room(db: Session, room_id: int, room: RoomCreate):
    db_room = get_room_by_id(db, room_id)
    db_room.name = room.name
    db_room.capacity = room.capacity
    db_room.amenities = room.amenities
    db_room.availability = room.availability
    db.commit()
    db.refresh(db_room)
    return db_room

# Delete a room by ID
def delete_room(db: Session, room_id: int):
    db_room = get_room_by_id(db, room_id)
    db.delete(db_room)
    db.commit()
    return db_room

def get_rooms(db: Session):
    return db.query(Room).all()