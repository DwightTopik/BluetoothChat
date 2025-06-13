package ru.vsu.cs.bluetoothchat.data.local

import ru.vsu.cs.bluetoothchat.domain.chat.BluetoothMessage

fun ChatMessageEntity.toBluetoothMessage(): BluetoothMessage {
    return BluetoothMessage(
        message = message,
        senderName = senderName,
        isFromLocalUser = isFromLocalUser
    )
}

fun BluetoothMessage.toEntity(deviceAddress: String): ChatMessageEntity {
    return ChatMessageEntity(
        deviceAddress = deviceAddress,
        message = message,
        senderName = senderName,
        isFromLocalUser = isFromLocalUser,
        timestamp = System.currentTimeMillis()
    )
}
