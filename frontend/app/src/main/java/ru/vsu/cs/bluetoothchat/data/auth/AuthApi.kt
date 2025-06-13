package ru.vsu.cs.bluetoothchat.data.auth

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("users/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("users/login_json")
    suspend fun login(@Body request: LoginRequest): AuthResponse
} 