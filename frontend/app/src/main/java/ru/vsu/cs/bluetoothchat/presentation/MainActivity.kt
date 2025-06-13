package ru.vsu.cs.bluetoothchat.presentation

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vsu.cs.bluetoothchat.R
import ru.vsu.cs.bluetoothchat.presentation.components.AuthScreen
import ru.vsu.cs.bluetoothchat.presentation.components.ChatScreen
import ru.vsu.cs.bluetoothchat.presentation.components.DeviceScreen
import ru.vsu.cs.bluetoothchat.theme.BluetoothChatTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.viewmodel.compose.viewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { /* Not needed */ }

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            val canEnableBluetooth = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else true

            if(canEnableBluetooth && !isBluetoothEnabled) {
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        }

        setContent {
            BluetoothChatTheme {
                val prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
                val isAuthorized = prefs.getString("jwt_token", null) != null
                val lastDeviceAddress = prefs.getString("last_device_address", null)
                val showAuthState = remember { mutableStateOf(!isAuthorized) }
                val showAuth by showAuthState

                if (showAuth) {
                    val authViewModel: AuthViewModel = viewModel()
                    val authState by authViewModel.authState.collectAsState()
                    AuthScreen(
                        onRegister = { username, email, password, confirmPassword ->
                            if (password == confirmPassword) {
                                authViewModel.register(username, email, password)
                            }
                        },
                        onLogin = { login, password ->
                            authViewModel.login(login, password)
                        },
                        isLoading = false,
                        errorMessage = authState?.errorMessage
                    )
                    LaunchedEffect(authState) {
                        if (authState?.success == true && authState?.token != null) {
                            prefs.edit().putString("jwt_token", authState?.token).apply()
                            showAuthState.value = false
                        }
                    }
                } else {
                    val viewModel = hiltViewModel<BluetoothViewModel>()
                    val state by viewModel.state.collectAsState()

                    LaunchedEffect(lastDeviceAddress) {
                        lastDeviceAddress?.let { viewModel.observeMessagesForDevice(it) }
                    }

                    LaunchedEffect(key1 = state.errorMessage) {
                        state.errorMessage?.let { message ->
                            Toast.makeText(
                                applicationContext,
                                message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    LaunchedEffect(key1 = state.isConnected) {
                        if(state.isConnected) {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.connected),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    Surface(
                        color = MaterialTheme.colors.background
                    ) {
                        when {
                            state.isConnecting -> {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator()
                                    Text(text = stringResource(R.string.connecting))
                                }
                            }
                            state.isConnected -> {
                                ChatScreen(
                                    viewModel = viewModel,
                                    state = state,
                                    onDisconnect = viewModel::disconnectFromDevice,
                                    onSendMessage = viewModel::sendMessage
                                )
                            }
                            else -> {
                                DeviceScreen(
                                    state = state,
                                    onStartScan = viewModel::startScan,
                                    onStopScan = viewModel::stopScan,
                                    onDeviceClick = {
                                        prefs.edit().putString("last_device_address", it.address).apply()
                                        viewModel.connectToDevice(it)
                                    },
                                    onStartServer = viewModel::waitForIncomingConnections
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}