from pydantic import BaseModel
from datetime import datetime
from typing import Optional

# Базовая схема сообщения
class MessageBase(BaseModel):
    content: str
    receiver_device_id: int

# Схема для создания сообщения
class MessageCreate(MessageBase):
    pass

# Схема для обновления сообщения
class MessageUpdate(BaseModel):
    is_delivered: Optional[int] = None
    delivered_at: Optional[datetime] = None

# Схема для отображения сообщения
class MessageResponse(BaseModel):
    id: int
    content: str
    is_delivered: int
    sent_at: datetime
    delivered_at: Optional[datetime] = None
    sender_device_id: int
    receiver_device_id: int
    
    class Config:
        from_attributes = True

# Схема для синхронизации сообщений
class MessageSync(BaseModel):
    messages: list[MessageResponse]
    last_sync_time: datetime 