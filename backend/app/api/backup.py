from fastapi import APIRouter

router = APIRouter(prefix="/backup", tags=["backup"])

@router.post("/export")
def export_backup():
    return {"msg": "Экспорт резервной копии (заглушка)"}

@router.post("/import")
def import_backup():
    return {"msg": "Импорт резервной копии (заглушка)"} 