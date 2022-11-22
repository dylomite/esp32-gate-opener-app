package com.dylomite.gateopener.repo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

object PermissionRepo {

    fun getNeededBluetoothPermissions() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    fun hasBluetoothPermission(context: Context): Boolean {
        var hasPermission = true
        val permArr = getNeededBluetoothPermissions()
        for (perm in permArr.iterator()) {
            hasPermission = ActivityCompat
                .checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission)
                break
        }
        return hasPermission
    }
}