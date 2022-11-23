package com.dylomite.gateopener.model.error

sealed class ErrorType(val code: Long, val showDismissButton: Boolean = false) {
    object ErrorGeneric : ErrorType(code = 1000)
    object ErrorBluetoothPerm : ErrorType(1001)
    object ErrorBluetoothPermMultiple : ErrorType(1002)
    object ErrorBluetoothNotSupported : ErrorType(1003)
    object ErrorNoPairedDevices : ErrorType(1004)
    object ErrorDeviceNotPaired : ErrorType(1005)
    object ErrorDeviceDisconnected : ErrorType(1006)
    object ErrorGattServiceNull : ErrorType(1007)
    object ErrorGattCharacteristicNull : ErrorType(1008)
}
