from fastapi import APIRouter

router = APIRouter(prefix="/messages", tags=["messages"])

@router.get("")
def get_messages():
    return {"msg": "История сообщений (заглушка)"}

@router.post("")
def send_message():
    return {"msg": "Отправить сообщение (заглушка)"} 