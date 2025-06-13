package ru.vsu.cs.bluetoothchat.di


import android.content.Context
import androidx.room.Room
import ru.vsu.cs.bluetoothchat.data.local.BluetoothChatDatabase
import ru.vsu.cs.bluetoothchat.data.local.ChatMessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BluetoothChatDatabase {
        return Room.databaseBuilder(
            context,
            BluetoothChatDatabase::class.java,
            "bluetooth_chat_db"
        ).build()
    }

    @Provides
    fun provideChatMessageDao(db: BluetoothChatDatabase): ChatMessageDao = db.chatMessageDao()
}
