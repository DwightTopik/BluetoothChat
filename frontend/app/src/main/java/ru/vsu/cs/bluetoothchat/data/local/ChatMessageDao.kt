package ru.vsu.cs.bluetoothchat.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM messages WHERE deviceAddress = :address ORDER BY timestamp ASC")
    fun getMessagesForDevice(address: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM messages WHERE deviceAddress = :address")
    suspend fun deleteMessagesForDevice(address: String)
}
