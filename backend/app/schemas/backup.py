from pydantic import BaseModel
from datetime import datetime
from typing import Optional, List

# Схема для создания резервной копии
class BackupCreate(BaseModel):
    filename: str

# Схема для отображения резервной копии
class BackupResponse(BaseModel):
    id: int
    filename: str
    created_at: datetime
    user_id: int
    
    class Config:
        from_attributes = True

# Схема для списка резервных копий
class BackupList(BaseModel):
    backups: List[BackupResponse] 