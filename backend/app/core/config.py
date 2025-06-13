import os
from typing import Optional

from pydantic import BaseModel
from dotenv import load_dotenv

# Загружаем переменные окружения из .env файла, если он существует
load_dotenv()

class Settings(BaseModel):
    """
    Настройки приложения.
    Значения загружаются из переменных окружения или используются значения по умолчанию.
    """
    # Настройки базы данных
    DATABASE_URL: str = os.getenv("DATABASE_URL")
    
    # JWT настройки
    SECRET_KEY: str = os.getenv("SECRET_KEY")
    ALGORITHM: str = os.getenv("ALGORITHM")
    ACCESS_TOKEN_EXPIRE_MINUTES: int = int(os.getenv("ACCESS_TOKEN_EXPIRE_MINUTES", "60"))
    
    # Настройки приложения
    APP_NAME: str = "BluetoothChat API"
    DEBUG: bool = os.getenv("DEBUG", "False").lower() == "true"
    
    # Другие настройки
    PUSH_NOTIFICATIONS_ENABLED: bool = os.getenv("PUSH_NOTIFICATIONS_ENABLED", "True").lower() == "true"

# Создаем экземпляр настроек для использования в приложении
settings = Settings() 