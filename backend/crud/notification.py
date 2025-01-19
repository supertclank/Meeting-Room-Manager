from sqlalchemy.orm import Session
from models.notification import Notification
from schemas.notification import NotificationCreate

# Create a new notification
def create_notification(db: Session, notification: NotificationCreate):
    db_notification = Notification(title=notification.title, description=notification.description)
    db.add(db_notification)
    db.commit()
    db.refresh(db_notification)
    return db_notification

# Read a notification by ID
def get_notification_by_id(db: Session, notification_id: int):
    return db.query(Notification).filter(Notification.id == notification_id).first()

# Update a notification by ID
def update_notification(db: Session, notification_id: int, notification: NotificationCreate):
    db_notification = get_notification_by_id(db, notification_id)
    db_notification.title = notification.title
    db_notification.description = notification.description
    db.commit()
    db.refresh(db_notification)
    return db_notification

# Delete a notification by ID
def delete_notification(db: Session, notification_id: int):
    db_notification = get_notification_by_id(db, notification_id)
    db.delete(db_notification)
    db.commit()
    return db_notification