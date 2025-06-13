package ru.vsu.cs.bluetoothchat.data.auth

interface AuthRepository {
    suspend fun register(username: String, email: String, password: String): AuthResult
    suspend fun login(usernameOrEmail: String, password: String): AuthResult
} 