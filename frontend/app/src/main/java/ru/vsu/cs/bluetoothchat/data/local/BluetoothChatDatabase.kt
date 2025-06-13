package ru.vsu.cs.bluetoothchat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ChatMessageEntity::class],
    version = 1
)
abstract class BluetoothChatDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
}
