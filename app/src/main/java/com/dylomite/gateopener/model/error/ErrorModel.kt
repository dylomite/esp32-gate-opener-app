package com.dylomite.gateopener.model.error

import android.content.Context
import com.dylomite.gateopener.R

class ErrorModel(
    private val errorType: ErrorType = ErrorType.ErrorGeneric,
    var showDiscardButton: Boolean = errorType.showDismissButton,
    var customMessage: String? = null,
    var onActionButtonPressed: () -> Unit = { },
    var onDiscardButtonPressed: () -> Unit = { },
    var onDismissButtonPressed: () -> Unit = { },
) {

    fun getMessage(context: Context): String {
        val strRes = when (errorType) {
            ErrorType.ErrorGeneric -> R.string.error_generic
            ErrorType.ErrorBluetoothNotSupported -> R.string.error_bluetooth_not_supported
            ErrorType.ErrorNoPairedDevices -> R.string.error_no_paired_devices
            ErrorType.ErrorDeviceNotPaired -> R.string.error_device_not_paired
            ErrorType.ErrorDeviceDisconnected -> R.string.error_device_disconnected
            ErrorType.ErrorGattServiceNull -> R.string.error_gatt_service_null
            ErrorType.ErrorGattCharacteristicNull -> R.string.error_gatt_characteristic_null
            else -> R.string.error_generic
        }

        return context.getString(strRes)
            .let { str ->
                if (!customMessage.isNullOrEmpty()) {
                    str.plus("\n\n").plus(customMessage)
                } else {
                    str
                }
            }
            .plus("\n\n")
            .plus("[${errorType.code}]")
    }

}