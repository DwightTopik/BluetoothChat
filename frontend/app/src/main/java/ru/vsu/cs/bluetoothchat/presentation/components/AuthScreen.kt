package ru.vsu.cs.bluetoothchat.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import android.app.Activity
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.TextFieldDefaults
import ru.vsu.cs.bluetoothchat.theme.Cyan700
import ru.vsu.cs.bluetoothchat.theme.DarkBlue
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle

@Composable
fun AuthScreen(
    onRegister: (String, String, String, String) -> Unit,
    onLogin: (String, String) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    var isRegister by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    // Установка цвета StatusBar
    val view = LocalView.current
    SideEffect {
        val window = (view.context as? Activity)?.window
        window?.statusBarColor = DarkBlue.toArgb()
    }

    Surface(
        color = DarkBlue,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isRegister) {
                Text(text = "Регистрация", style = MaterialTheme.typography.h5, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Имя пользователя", color = Color.White) },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        cursorColor = Cyan700,
                        focusedBorderColor = Cyan700,
                        unfocusedBorderColor = Cyan700,
                        focusedLabelColor = Cyan700,
                        unfocusedLabelColor = Cyan700
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = Color.White) },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        cursorColor = Cyan700,
                        focusedBorderColor = Cyan700,
                        unfocusedBorderColor = Cyan700,
                        focusedLabelColor = Cyan700,
                        unfocusedLabelColor = Cyan700
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Пароль", color = Color.White) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        cursorColor = Cyan700,
                        focusedBorderColor = Cyan700,
                        unfocusedBorderColor = Cyan700,
                        focusedLabelColor = Cyan700,
                        unfocusedLabelColor = Cyan700
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Повторите пароль", color = Color.White) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        cursorColor = Cyan700,
                        focusedBorderColor = Cyan700,
                        unfocusedBorderColor = Cyan700,
                        focusedLabelColor = Cyan700,
                        unfocusedLabelColor = Cyan700
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                PasswordRequirements(password = password)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                            errorText = "Заполните все поля"
                        } else if (password != confirmPassword) {
                            errorText = "Пароли не совпадают"
                        } else {
                            errorText = null
                            onRegister(username, email, password, confirmPassword)
                        }
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Cyan700)
                ) {
                    Text("Зарегистрироваться", color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { isRegister = false }) {
                    Text("Уже есть аккаунт? Войти", color = Cyan700)
                }
            } else {
                Text(text = "Вход", style = MaterialTheme.typography.h5, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it },
                    label = { Text("Email или имя пользователя", color = Color.White) },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        cursorColor = Cyan700,
                        focusedBorderColor = Cyan700,
                        unfocusedBorderColor = Cyan700,
                        focusedLabelColor = Cyan700,
                        unfocusedLabelColor = Cyan700
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = loginPassword,
                    onValueChange = { loginPassword = it },
                    label = { Text("Пароль", color = Color.White) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        cursorColor = Cyan700,
                        focusedBorderColor = Cyan700,
                        unfocusedBorderColor = Cyan700,
                        focusedLabelColor = Cyan700,
                        unfocusedLabelColor = Cyan700
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (login.isBlank() || loginPassword.isBlank()) {
                            errorText = "Заполните все поля"
                        } else {
                            errorText = null
                            onLogin(login, loginPassword)
                        }
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Cyan700)
                ) {
                    Text("Войти", color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { isRegister = true }) {
                    Text(
                        buildAnnotatedString {
                            append("Нет аккаунта? ")
                            withStyle(
                                style = SpanStyle(
                                    color = Cyan700
                                )
                            ) {
                                append("Зарегистрироваться")
                            }
                        },
                        color = Color(0xFF90A4AE) // серый цвет для остального текста
                    )
                }
            }
            if (errorText != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorText ?: "", color = Color.Red)
            } else if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage, color = MaterialTheme.colors.error)
            }
        }
    }
}

@Composable
fun PasswordRequirements(password: String) {
    val minLength = password.length >= 8
    val hasLower = password.any { it.isLowerCase() }
    val hasUpper = password.any { it.isUpperCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecial = password.any { !it.isLetterOrDigit() }
    val noTripleRepeat = !Regex("(.)\\1{2,}").containsMatchIn(password)
    val categories = listOf(hasLower, hasUpper, hasDigit, hasSpecial).count { it }
    val enoughCategories = categories >= 3

    val requirements = listOf(
        Triple("Не менее 8 символов", minLength, true),
        Triple("Не более 2 одинаковых символов подряд", noTripleRepeat, true),
        Triple("Не менее 3 из 4 категорий:", enoughCategories, false),
        Triple("- строчные буквы (a-z)", hasLower, false),
        Triple("- прописные буквы (A-Z)", hasUpper, false),
        Triple("- цифры (0-9)", hasDigit, false),
        Triple("- спецсимволы (!@#%$^&*)", hasSpecial, false)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Пароль должен содержать:",
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(4.dp))
        requirements.forEach { (text, valid, alwaysShow) ->
            if (alwaysShow || password.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (valid) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        tint = if (valid) Color(0xFF4CAF50) else Color(0xFFB0B0B0),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = text,
                        color = if (valid) Color(0xFF4CAF50) else Color(0xFFB0B0B0),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
} 