from pydantic import BaseModel, EmailStr, Field
from datetime import datetime
from typing import Optional, List

# Базовая схема пользователя
class UserBase(BaseModel):
    username: str
    email: EmailStr

# Схема для создания пользователя
class UserCreate(UserBase):
    password: str = Field(..., min_length=8, max_length=100)

# Схема для обновления пользователя
class UserUpdate(BaseModel):
    username: Optional[str] = None
    email: Optional[EmailStr] = None
    password: Optional[str] = Field(None, min_length=8, max_length=100)
    is_active: Optional[bool] = None

# Схема для отображения пользователя
class UserResponse(UserBase):
    id: int
    is_active: bool
    created_at: datetime
    
    class Config:
        from_attributes = True

# Схемы для аутентификации
class Token(BaseModel):
    access_token: str
    token_type: str = "bearer"

class TokenPayload(BaseModel):
    sub: str
    exp: int

class UserLogin(BaseModel):
    username: str
    password: str 