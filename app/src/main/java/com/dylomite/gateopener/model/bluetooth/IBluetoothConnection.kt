package com.dylomite.gateopener.model.bluetooth

import android.bluetooth.BluetoothGatt

interface IBluetoothConnection {
    fun getBluetoothGatt(): BluetoothGatt?
    fun onDisconnect()
}