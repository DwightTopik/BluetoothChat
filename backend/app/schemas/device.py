from pydantic import BaseModel
from datetime import datetime
from typing import Optional

# Базовая схема устройства
class DeviceBase(BaseModel):
    name: str
    bluetooth_address: str

# Схема для создания устройства
class DeviceCreate(DeviceBase):
    pass

# Схема для обновления устройства
class DeviceUpdate(BaseModel):
    name: Optional[str] = None
    is_online: Optional[bool] = None

# Схема для отображения устройства
class DeviceResponse(DeviceBase):
    id: int
    is_online: bool
    last_seen: datetime
    created_at: datetime
    user_id: int
    
    class Config:
        from_attributes = True

# Схема для обновления статуса устройства
class DeviceStatus(BaseModel):
    is_online: bool 