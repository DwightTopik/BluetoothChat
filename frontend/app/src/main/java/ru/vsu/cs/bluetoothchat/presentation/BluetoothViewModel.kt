package ru.vsu.cs.bluetoothchat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.vsu.cs.bluetoothchat.domain.chat.BluetoothController
import ru.vsu.cs.bluetoothchat.domain.chat.BluetoothDeviceDomain
import ru.vsu.cs.bluetoothchat.domain.chat.ConnectionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import ru.vsu.cs.bluetoothchat.data.local.ChatMessageDao
import ru.vsu.cs.bluetoothchat.data.local.toBluetoothMessage
import ru.vsu.cs.bluetoothchat.data.local.toEntity
import ru.vsu.cs.bluetoothchat.data.chat.MessagesRepository
import ru.vsu.cs.bluetoothchat.data.chat.SendMessageRequest
import ru.vsu.cs.bluetoothchat.domain.chat.BluetoothMessage


@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val bluetoothController: BluetoothController,
    private val chatMessageDao: ChatMessageDao,
    private val messagesRepository: MessagesRepository
): ViewModel() {

    private var currentDeviceAddress: String? = null

    private val _state = MutableStateFlow(BluetoothUiState())
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state
    ) { scannedDevices, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    val messagesFlow = MutableStateFlow<List<BluetoothMessage>>(emptyList())

    private var deviceConnectionJob: Job? = null

    init {
        bluetoothController.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)

        bluetoothController.errors.onEach { error ->
            _state.update { it.copy(
                errorMessage = error
            ) }
        }.launchIn(viewModelScope)
    }

    fun connectToDevice(device: BluetoothDeviceDomain) {
        _state.update { it.copy(isConnecting = true) }
        currentDeviceAddress = device.address

        viewModelScope.launch {
            val localDeviceId = device.address.toIntOrNull() ?: return@launch
            val remoteDeviceId = localDeviceId
            messagesRepository.syncHistory(localDeviceId, remoteDeviceId)
            observeMessagesForDevice(device.address)
        }

        deviceConnectionJob = bluetoothController
            .connectToDevice(device)
            .listen()
    }


    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()
        _state.update { it.copy(
            isConnecting = false,
            isConnected = false
        ) }
    }

    fun waitForIncomingConnections() {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController
            .startBluetoothServer()
            .listen()
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val bluetoothMessage = bluetoothController.trySendMessage(message)
            currentDeviceAddress?.let { address ->
                bluetoothMessage?.let {
                    chatMessageDao.insertMessage(it.toEntity(address))
                    val localDeviceId = address.toIntOrNull() ?: return@let
                    val remoteDeviceId = localDeviceId
                    messagesRepository.sendMessage(
                        SendMessageRequest(
                            content = it.message,
                            sender_device_id = localDeviceId,
                            receiver_device_id = remoteDeviceId
                        )
                    )
                }
            }
            if(bluetoothMessage != null) {
                _state.update { it.copy(
                    messages = it.messages + bluetoothMessage
                ) }
            }
        }
    }


    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController.stopDiscovery()
    }

    fun clearHistory() {
        currentDeviceAddress?.let { address ->
            viewModelScope.launch {
                chatMessageDao.deleteMessagesForDevice(address)
            }
        }
    }

    fun observeMessagesForDevice(address: String) {
        viewModelScope.launch {
            chatMessageDao.getMessagesForDevice(address)
                .collect { list ->
                    messagesFlow.value = list.map { it.toBluetoothMessage() }
                }
        }
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when(result) {
                ConnectionResult.ConnectionEstablished -> {
                    _state.update { it.copy(
                        isConnected = true,
                        isConnecting = false,
                        errorMessage = null
                    ) }
                }
                is ConnectionResult.TransferSucceeded -> {
                    currentDeviceAddress?.let { address ->
                        chatMessageDao.insertMessage(result.message.toEntity(address))
                    }
                    _state.update { it.copy(
                        messages = it.messages + result.message
                    ) }
                }
                is ConnectionResult.Error -> {
                    _state.update { it.copy(
                        isConnected = false,
                        isConnecting = false,
                        errorMessage = result.message
                    ) }
                }
            }
        }
            .catch { throwable ->
                bluetoothController.closeConnection()
                _state.update { it.copy(
                    isConnected = false,
                    isConnecting = false,
                ) }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothController.release()
    }
}