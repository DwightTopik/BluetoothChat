from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.db.database import get_db
from app.models.message import Message
from app.schemas.message import MessageCreate, MessageResponse
from datetime import datetime
from typing import List

router = APIRouter(prefix="/messages", tags=["messages"])

@router.post("/send", response_model=MessageResponse)
async def send_message(
    message_in: MessageCreate,
    db: AsyncSession = Depends(get_db),
    # current_user: User = Depends(get_current_user) # если нужна авторизация
):
    # sender_device_id должен приходить с клиента или определяться по токену/сессии
    # Здесь для примера берём sender_device_id из message_in (или доработать под свою логику)
    msg = Message(
        content=message_in.content,
        sender_device_id=message_in.sender_device_id,
        receiver_device_id=message_in.receiver_device_id,
        sent_at=datetime.utcnow(),
        is_delivered=0
    )
    db.add(msg)
    await db.commit()
    await db.refresh(msg)
    return msg

@router.get("/history", response_model=List[MessageResponse])
async def get_history(
    device1: int, device2: int, db: AsyncSession = Depends(get_db)
):
    # История между двумя устройствами (в обе стороны)
    result = await db.execute(
        select(Message).where(
            ((Message.sender_device_id == device1) & (Message.receiver_device_id == device2)) |
            ((Message.sender_device_id == device2) & (Message.receiver_device_id == device1))
        ).order_by(Message.sent_at.asc())
    )
    messages = result.scalars().all()
    return messages 