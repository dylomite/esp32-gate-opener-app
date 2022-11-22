package com.dylomite.gateopener.model.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.util.Log

@SuppressLint("MissingPermission")
class BluetoothCallbacks(
    val onConnect: (gatt: BluetoothGatt) -> Unit,
    val onDisconnect: (gatt: BluetoothGatt) -> Unit
) : BluetoothGattCallback() {

    companion object {
        const val TAG = "BluetoothCallbacks"
    }

    override fun onConnectionStateChange(
        gatt: BluetoothGatt,
        status: Int,
        newState: Int
    ) {
        val deviceName = gatt.device.name
        when (status) {
            BluetoothGatt.GATT_SUCCESS -> {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d(TAG, "Successfully connected to $deviceName")
                    onConnect(gatt)
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d(TAG, "Disconnected from $deviceName")
                    onDisconnect(gatt)
                }
            }
            else -> {
                Log.d(TAG, "Error $status [device: $deviceName]. Disconnecting...")
                onDisconnect(gatt)
            }
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        Log.d(TAG, "onServicesDiscovered status=$status [${gatt.services.size}]")
    }
}