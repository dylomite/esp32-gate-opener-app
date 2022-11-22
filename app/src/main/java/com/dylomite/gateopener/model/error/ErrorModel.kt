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