package com.dylomite.gateopener.model.error

sealed class ErrorCode {
    object ErrorGeneric : ErrorCode()
    object ErrorAdapterNull : ErrorCode()
    object ErrorDeviceNotBonded : ErrorCode()
}
