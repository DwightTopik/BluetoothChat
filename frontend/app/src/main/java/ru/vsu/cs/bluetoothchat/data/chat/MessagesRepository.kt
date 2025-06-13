package ru.vsu.cs.bluetoothchat.data.chat

import ru.vsu.cs.bluetoothchat.data.local.ChatMessageDao
import ru.vsu.cs.bluetoothchat.data.local.ChatMessageEntity
import javax.inject.Inject

class MessagesRepository @Inject constructor(
    private val api: MessagesApi,
    private val dao: ChatMessageDao
) {
    suspend fun sendMessage(request: SendMessageRequest): MessageResponse {
        val response = api.sendMessage(request)
        dao.insertMessage(
            ChatMessageEntity(
                deviceAddress = response.receiver_device_id.toString(),
                message = response.content,
                senderName = response.sender_device_id.toString(),
                isFromLocalUser = true,
                timestamp = System.currentTimeMillis()
            )
        )
        return response
    }

    suspend fun syncHistory(device1: Int, device2: Int) {
        val messages = api.getHistory(device1, device2)
        messages.forEach { msg ->
            dao.insertMessage(
                ChatMessageEntity(
                    deviceAddress = if (msg.sender_device_id == device1) msg.receiver_device_id.toString() else msg.sender_device_id.toString(),
                    message = msg.content,
                    senderName = msg.sender_device_id.toString(),
                    isFromLocalUser = msg.sender_device_id == device1,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
} 