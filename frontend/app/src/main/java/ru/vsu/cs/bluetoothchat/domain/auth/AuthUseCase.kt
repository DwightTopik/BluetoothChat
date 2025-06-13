package ru.vsu.cs.bluetoothchat.domain.auth

import ru.vsu.cs.bluetoothchat.data.auth.AuthRepository
import ru.vsu.cs.bluetoothchat.data.auth.AuthResult
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend fun register(username: String, email: String, password: String): AuthResult {
        return repository.register(username, email, password)
    }
    suspend fun login(usernameOrEmail: String, password: String): AuthResult {
        return repository.login(usernameOrEmail, password)
    }
} 