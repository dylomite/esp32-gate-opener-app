package com.dylomite.gateopener.repo

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.util.Log

@SuppressLint("MissingPermission")
object BluetoothRepo {

    private const val TAG = "BluetoothRepo"

    fun BluetoothDevice.isBonded() = this.bondState == BluetoothDevice.BOND_BONDED

    private fun getBluetoothManager(context: Context): BluetoothManager? {
        return try {
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        } catch (e: Exception) {
            Log.e(TAG, "getBluetoothManager: ", e)
            null
        }
    }

    fun getBluetoothAdapter(context: Context) = getBluetoothManager(context)?.adapter

}