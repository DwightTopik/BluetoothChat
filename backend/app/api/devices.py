from fastapi import APIRouter

router = APIRouter(prefix="/devices", tags=["devices"])

@router.get("")
def list_devices():
    return {"msg": "Список устройств (заглушка)"}

@router.post("")
def add_device():
    return {"msg": "Добавить устройство (заглушка)"} 