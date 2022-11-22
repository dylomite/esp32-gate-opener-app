package com.dylomite.gateopener.repo

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.*
import android.content.Context
import android.util.Log

@SuppressLint("MissingPermission")
object BluetoothRepo {

    private const val TAG = "BluetoothRepo"

    fun BluetoothDevice.isBonded() = this.bondState == BluetoothDevice.BOND_BONDED

    fun getBluetoothManager(activity: Activity): BluetoothManager? {
        return try {
            activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        } catch (e: Exception) {
            Log.e(TAG, "getBluetoothManager: ", e)
            null
        }
    }

    fun getBluetoothAdapter(activity: Activity) = getBluetoothManager(activity)?.adapter

}