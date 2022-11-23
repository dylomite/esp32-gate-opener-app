package com.dylomite.gateopener.bluetooth

import android.bluetooth.BluetoothGatt

interface IBluetoothConnection {
    fun getBluetoothGatt(): BluetoothGatt?
    fun onDisconnect()
}