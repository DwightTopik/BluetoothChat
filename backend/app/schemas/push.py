from pydantic import BaseModel
from datetime import datetime
from typing import Optional, List

# Схема для токена push-уведомлений
class PushTokenCreate(BaseModel):
    token: str
    device_type: str

class PushTokenResponse(BaseModel):
    id: int
    token: str
    device_type: str
    created_at: datetime
    device_id: int
    
    class Config:
        from_attributes = True

# Схема для push-уведомления
class PushNotificationCreate(BaseModel):
    title: str
    body: str
    device_id: int

class PushNotificationResponse(BaseModel):
    id: int
    title: str
    body: str
    is_sent: bool
    created_at: datetime
    sent_at: Optional[datetime] = None
    device_id: int
    
    class Config:
        from_attributes = True

# Схема для отправки push-уведомления
class PushSend(BaseModel):
    title: str
    body: str
    device_ids: List[int] 