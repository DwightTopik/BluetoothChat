package ru.vsu.cs.bluetoothchat.data.auth

data class AuthResponse(
    val access_token: String?,
    val token_type: String?,
    val detail: String? = null
) 