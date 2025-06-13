from fastapi import APIRouter

router = APIRouter(prefix="/push", tags=["push"])
 
@router.post("/send")
def send_push():
    return {"msg": "Отправить push-уведомление (заглушка)"} 