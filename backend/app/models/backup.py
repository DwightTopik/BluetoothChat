from datetime import datetime
from sqlalchemy import Column, Integer, String, DateTime, ForeignKey, LargeBinary
from sqlalchemy.orm import relationship

from app.db.database import Base

class Backup(Base):
    """
    Модель резервной копии.
    Хранит информацию о резервной копии: файл, пользователь, дата создания.
    """
    __tablename__ = "backups"

    id = Column(Integer, primary_key=True, index=True)
    filename = Column(String, nullable=False)
    data = Column(LargeBinary, nullable=False)  # Содержимое резервной копии
    created_at = Column(DateTime, default=datetime.utcnow)
    
    # Отношения
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    user = relationship("User") 