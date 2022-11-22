package com.dylomite.gateopener.repo

import android.app.Activity
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log

object BluetoothRepo {

    private const val TAG = "BluetoothRepo"

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