from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
import logging

from schemas.notification import NotificationCreate, NotificationRead
from crud.notification import create_notification, get_notification_by_id, delete_notification
from utils.database import get_db

# Initialize the logger
logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

router = APIRouter()

@router.post("/", response_model=NotificationRead)
def create_new_notification(
    notification: NotificationCreate,
    db: Session = Depends(get_db)
):
    return create_notification(db, notification)

@router.get("/{notification_id}", response_model=NotificationRead)
def read_notification_by_id(
    notification_id: int,
    db: Session = Depends(get_db)
):
    notification = get_notification_by_id(db, notification_id)
    if notification is None:
        logger.warning(f"Notification with ID: {notification_id} not found")
        raise HTTPException(status_code=404, detail="Notification not found")
    return notification

@router.delete("/{notification_id}", response_model=NotificationRead)
def delete_notification_by_id(notification_id: int, db: Session = Depends(get_db)):
    logger.info(f"Deleting notification with ID: {notification_id}")
    notification = get_notification_by_id(db, notification_id)
    if notification is None:
        logger.warning(f"Notification with ID: {notification_id} not found")
        raise HTTPException(status_code=404, detail="Notification not found")
    return delete_notification(db, notification_id)