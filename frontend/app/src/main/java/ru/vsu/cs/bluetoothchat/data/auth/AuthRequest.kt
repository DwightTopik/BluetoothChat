package ru.vsu.cs.bluetoothchat.data.auth

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
) 