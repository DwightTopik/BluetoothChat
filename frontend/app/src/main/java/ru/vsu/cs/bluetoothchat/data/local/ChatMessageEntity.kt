package ru.vsu.cs.bluetoothchat.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deviceAddress: String,
    val message: String,
    val senderName: String,
    val isFromLocalUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
