# FastAPI imports
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import uvicorn

from router.users import router as users_router
from router.booking import router as bookings_router
from router.room import router as rooms_router
from router.login import router as login_router
from router.notification import router as notification_router

import logging

# Initialize the logger
logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

app = FastAPI()

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Allow all origins; change to specific URLs for production
    allow_credentials=True,
    allow_methods=["*"],  # Allow all HTTP methods
    allow_headers=["*"]   # Allow all headers
)
logger.info("CORS middleware has been added successfully.")

# Routers for endpoints
logger.info("Including routers for users, bookings, rooms, login, and notifications.")
app.include_router(users_router, prefix="/users", tags=["users"])
logger.info("Users router included.")

app.include_router(bookings_router, prefix="/bookings", tags=["bookings"])
logger.info("Bookings router included.")

app.include_router(rooms_router, prefix="/rooms", tags=["rooms"])
logger.info("Rooms router included.")

app.include_router(login_router, prefix="/login", tags=["login"])
logger.info("Login router included.")

app.include_router(notification_router, prefix="/notifications", tags=["notifications"])
logger.info("Notifications router included.")

# Entry point to run the server
if __name__ == "__main__":
    logger.info("Starting the FastAPI application...")
    uvicorn.run(app, host="127.0.0.1", port=8000)
    logger.info("FastAPI application running at http://127.0.0.1:8000")