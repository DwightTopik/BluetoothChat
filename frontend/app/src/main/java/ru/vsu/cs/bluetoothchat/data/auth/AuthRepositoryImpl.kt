package ru.vsu.cs.bluetoothchat.data.auth

import javax.inject.Inject
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun register(username: String, email: String, password: String): AuthResult {
        return try {
            val response = api.register(RegisterRequest(username, email, password))
            if (!response.access_token.isNullOrEmpty()) {
                AuthResult(success = true, token = response.access_token)
            } else {
                AuthResult(success = false, errorMessage = response.detail ?: "Неизвестная ошибка регистрации")
            }
        } catch (e: HttpException) {
            AuthResult(success = false, errorMessage = e.response()?.errorBody()?.string() ?: "Ошибка регистрации")
        } catch (e: IOException) {
            AuthResult(success = false, errorMessage = "Проблема с сетью")
        } catch (e: Exception) {
            AuthResult(success = false, errorMessage = e.localizedMessage ?: "Неизвестная ошибка")
        }
    }
    override suspend fun login(username: String, password: String): AuthResult {
        return try {
            val response = api.login(LoginRequest(username, password))
            if (!response.access_token.isNullOrEmpty()) {
                AuthResult(success = true, token = response.access_token)
            } else {
                AuthResult(success = false, errorMessage = response.detail ?: "Неверные данные для входа")
            }
        } catch (e: HttpException) {
            AuthResult(success = false, errorMessage = e.response()?.errorBody()?.string() ?: "Ошибка входа")
        } catch (e: IOException) {
            AuthResult(success = false, errorMessage = "Проблема с сетью")
        } catch (e: Exception) {
            AuthResult(success = false, errorMessage = e.localizedMessage ?: "Неизвестная ошибка")
        }
    }
} 