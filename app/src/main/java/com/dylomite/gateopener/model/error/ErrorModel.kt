package com.dylomite.gateopener.model.error

import android.content.Context
import com.dylomite.gateopener.R

class ErrorModel(
    private val errorCode: ErrorCode = ErrorCode.ErrorGeneric,
    var customMessage: String? = null,
    var onOkButtonPressed: () -> Unit = { },
    var onDiscardButtonPressed: () -> Unit = { },
    var onDismissButtonPressed: () -> Unit = { },
) {

    fun getMessage(context: Context): String {
        return getLocalString(context).let { localErrStr ->
            if (!customMessage.isNullOrEmpty()) {
                localErrStr.plus("\n\n").plus(customMessage)
            } else {
                localErrStr
            }
        }
    }

    private fun getLocalString(context: Context): String {
        val strRes = when (errorCode) {
            ErrorCode.ErrorGeneric -> R.string.error_generic
            else -> R.string.error_generic
        }
        return "${context.getString(strRes)} - $errorCode"
    }

}