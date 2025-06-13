package ru.vsu.cs.bluetoothchat.data.chat

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class SendMessageRequest(
    val content: String,
    val sender_device_id: Int,
    val receiver_device_id: Int
)

data class MessageResponse(
    val id: Int,
    val content: String,
    val is_delivered: Int,
    val sent_at: String,
    val delivered_at: String?,
    val sender_device_id: Int,
    val receiver_device_id: Int
)

interface MessagesApi {
    @POST("messages/send")
    suspend fun sendMessage(@Body request: SendMessageRequest): MessageResponse

    @GET("messages/history")
    suspend fun getHistory(
        @Query("device1") device1: Int,
        @Query("device2") device2: Int
    ): List<MessageResponse>
} 