from datetime import datetime
from sqlalchemy import Column, Integer, String, Boolean, DateTime, ForeignKey
from sqlalchemy.orm import relationship

from app.db.database import Base

class Device(Base):
    """
    Модель устройства Bluetooth.
    Хранит информацию об устройстве: адрес, имя, статус, владелец.
    """
    __tablename__ = "devices"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, nullable=False)
    bluetooth_address = Column(String, unique=True, index=True, nullable=False)
    is_online = Column(Boolean, default=False)
    last_seen = Column(DateTime, default=datetime.utcnow)
    created_at = Column(DateTime, default=datetime.utcnow)
    
    # Отношения
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    user = relationship("User", back_populates="devices")
    
    # Сообщения
    sent_messages = relationship("Message", foreign_keys="Message.sender_device_id", back_populates="sender_device", cascade="all, delete-orphan")
    received_messages = relationship("Message", foreign_keys="Message.receiver_device_id", back_populates="receiver_device", cascade="all, delete-orphan") 