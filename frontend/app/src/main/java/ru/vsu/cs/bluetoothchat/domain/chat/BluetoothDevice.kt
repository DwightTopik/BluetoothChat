package ru.vsu.cs.bluetoothchat.domain.chat

typealias BluetoothDeviceDomain = BluetoothDevice

data class
BluetoothDevice(
    val name: String?,
    val address: String
)
