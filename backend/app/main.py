from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api import users, devices, messages, backup, push, message

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(users.router)
app.include_router(devices.router)
app.include_router(messages.router)
app.include_router(backup.router)
app.include_router(push.router)
app.include_router(message.router) 