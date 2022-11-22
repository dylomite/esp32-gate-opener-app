package com.example.btsample.model.error

import android.content.Context
import com.example.btsample.R

class ErrorModel(
    private val errorCode: ErrorCode = ErrorCode.ErrorGeneric,
    var customMessage: String? = null,
    var onOkButtonPressed: () -> Unit = { },
    var onDiscardButtonPressed: () -> Unit = { },
    var onDismissButtonPressed: () -> Unit = { },
) {


    fun getMessage(context: Context): String {
        return getLocalString(context).let { localStr ->
            var errorStr = localStr
            customMessage = null
            if (!customMessage.isNullOrEmpty()) {
                errorStr = localStr
                    .plus("\n\n")
                    .plus(customMessage)
            }
            errorStr
        }
    }

    private fun getLocalString(context: Context): String {
        var message: String? = null
        /**
         * Add errors if necessary
         */
        message = when (errorCode) {
            ErrorCode.ErrorGeneric -> context.getString(R.string.error_generic)
            else -> context.getString(R.string.error_generic)
        }

        return message?.let { msg -> "$errorCode - $msg" } ?: "Sorry there was a problem."
    }

}