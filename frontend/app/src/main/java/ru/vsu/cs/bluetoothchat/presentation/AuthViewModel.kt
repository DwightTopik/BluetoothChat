package ru.vsu.cs.bluetoothchat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.vsu.cs.bluetoothchat.domain.auth.AuthUseCase
import ru.vsu.cs.bluetoothchat.data.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
): ViewModel() {
    private val _authState = MutableStateFlow<AuthResult?>(null)
    val authState: StateFlow<AuthResult?> = _authState

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            val result = authUseCase.register(username, email, password)
            _authState.value = result
        }
    }

    fun login(usernameOrEmail: String, password: String) {
        viewModelScope.launch {
            val result = authUseCase.login(usernameOrEmail, password)
            _authState.value = result
        }
    }
} 