from datetime import datetime
from sqlalchemy import Column, Integer, String, Text, DateTime, ForeignKey, Boolean
from sqlalchemy.orm import relationship

from app.db.database import Base

class PushToken(Base):
    """
    Модель токена для push-уведомлений.
    Хранит информацию о токене устройства для отправки push-уведомлений.
    """
    __tablename__ = "push_tokens"

    id = Column(Integer, primary_key=True, index=True)
    token = Column(String, nullable=False, index=True, unique=True)
    device_type = Column(String, nullable=False)  # 'android', 'ios', etc.
    created_at = Column(DateTime, default=datetime.utcnow)
    
    # Отношения
    device_id = Column(Integer, ForeignKey("devices.id"), nullable=False)
    device = relationship("Device")

class PushNotification(Base):
    """
    Модель push-уведомления.
    Хранит историю отправленных push-уведомлений.
    """
    __tablename__ = "push_notifications"

    id = Column(Integer, primary_key=True, index=True)
    title = Column(String, nullable=False)
    body = Column(Text, nullable=False)
    is_sent = Column(Boolean, default=False)
    created_at = Column(DateTime, default=datetime.utcnow)
    sent_at = Column(DateTime, nullable=True)
    
    # Отношения
    device_id = Column(Integer, ForeignKey("devices.id"), nullable=False)
    device = relationship("Device") 