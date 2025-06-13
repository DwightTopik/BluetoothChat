package ru.vsu.cs.bluetoothchat.data.auth

data class AuthResult(
    val success: Boolean,
    val token: String? = null,
    val errorMessage: String? = null
) 