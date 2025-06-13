from datetime import datetime
from sqlalchemy import Column, Integer, String, Text, DateTime, ForeignKey
from sqlalchemy.orm import relationship

from app.db.database import Base

class Message(Base):
    """
    Модель сообщения.
    Хранит информацию о сообщении: содержание, отправитель, получатель, время.
    """
    __tablename__ = "messages"

    id = Column(Integer, primary_key=True, index=True)
    content = Column(Text, nullable=False)
    is_delivered = Column(Integer, default=0)  # 0 - не доставлено, 1 - доставлено, 2 - прочитано
    sent_at = Column(DateTime, default=datetime.utcnow)
    delivered_at = Column(DateTime, nullable=True)
    
    # Отношения
    sender_device_id = Column(Integer, ForeignKey("devices.id"), nullable=False)
    receiver_device_id = Column(Integer, ForeignKey("devices.id"), nullable=False)
    
    sender_device = relationship("Device", foreign_keys=[sender_device_id], back_populates="sent_messages")
    receiver_device = relationship("Device", foreign_keys=[receiver_device_id], back_populates="received_messages") 