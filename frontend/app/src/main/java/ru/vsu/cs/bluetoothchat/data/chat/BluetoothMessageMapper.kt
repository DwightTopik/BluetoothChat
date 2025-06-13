package ru.vsu.cs.bluetoothchat.data.chat

import ru.vsu.cs.bluetoothchat.domain.chat.BluetoothMessage
import android.content.Context
import ru.vsu.cs.bluetoothchat.R

fun String.toBluetoothMessage(isFromLocalUser: Boolean, context: Context): BluetoothMessage {
    val name = substringBeforeLast("#")
    val message = substringAfter("#")
    return BluetoothMessage(
        senderName = if (name.isNotEmpty()) name else context.getString(R.string.unknown_name),
        message = message,
        isFromLocalUser = isFromLocalUser
    )
}

fun BluetoothMessage.toByteArray(): ByteArray {
    return "$senderName#$message".encodeToByteArray()
}