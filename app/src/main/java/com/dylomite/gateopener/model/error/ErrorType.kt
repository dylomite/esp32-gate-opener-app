package com.dylomite.gateopener.model.error

sealed class ErrorType(val code: Long, val showDismissButton: Boolean = false) {
    object ErrorGeneric : ErrorType(code = 1000)
    object ErrorAdapterNull : ErrorType(1001)
    object ErrorDeviceNotBonded : ErrorType(1002)
}
