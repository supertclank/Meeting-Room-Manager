# FastAPI imports
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import uvicorn

from router.users import router as users_router
from router.bookings import router as bookings_router
from router.rooms import router as rooms_router
from router.login import router as login_router
from router.notification import router as notification_router

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Allow all origins; change to specific URLs for production
    allow_credentials=True,
    allow_methods=["*"],  # Allow all HTTP methods
    allow_headers=["*"]   # Allow all headers
)

#routers for endpoints

app.include_router(users_router, prefix="/users", tags=["users"])

app.include_router(bookings_router, prefix="/bookings", tags=["bookings"])

app.include_router(rooms_router, prefix="/rooms", tags=["rooms"])

app.include_router(login_router, prefix="/login", tags=["login"])

app.include_router(notification_router, prefix="/notifications", tags=["notifications"])

# Entry point to run the server
if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8000)