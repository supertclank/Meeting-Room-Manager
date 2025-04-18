from fastapi import FastAPI, HTTPException
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from sqlalchemy.exc import SQLAlchemyError

# Load the database URL from environment variable or use a default
SQLALCHEMY_DATABASE_URL = "mysql+pymysql://root@localhost:3306/Meeting_Room_Manager"

# Create the SQLAlchemy engine
engine = create_engine(SQLALCHEMY_DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

# Dependency to get the database session
def get_db():
    db = SessionLocal()
    try:
        yield db
    except SQLAlchemyError as e:
        db.rollback()
        # Log the actual SQLAlchemy error for better debugging
        print(f"Database error occurred: {str(e)}")
        raise HTTPException(status_code=500, detail="Database error occurred")
    finally:
        db.close()

# Initialize the FastAPI app
app = FastAPI()