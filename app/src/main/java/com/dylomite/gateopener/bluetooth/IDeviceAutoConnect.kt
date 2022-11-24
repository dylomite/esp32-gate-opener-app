package com.dylomite.gateopener.bluetooth

import android.bluetooth.BluetoothDevice

interface IDeviceAutoConnect {
    fun tryAutoConnect(device: BluetoothDevice)
}