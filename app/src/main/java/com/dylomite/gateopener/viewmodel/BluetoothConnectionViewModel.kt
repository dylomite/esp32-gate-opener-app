package com.dylomite.gateopener.viewmodel

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dylomite.gateopener.model.error.ErrorModel
import com.dylomite.gateopener.model.error.ErrorType
import com.dylomite.gateopener.repo.BluetoothRepo
import com.dylomite.gateopener.repo.PermissionRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BluetoothConnectionViewModel(app: Application, activity: ComponentActivity) :
    AndroidViewModel(app), IBaseViewModel {

    companion object {
        const val TAG = "BluetoothConnectionViewModel"
    }

    override var isLoading = mutableStateOf(false)
    override var error = mutableStateOf<ErrorModel?>(null)

    private var askSingleBtPermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                ComponentActivity.RESULT_OK -> setupBluetooth(activity)
                else -> error.value = ErrorModel(ErrorType.ErrorBluetoothPerm)
            }
        }

    private var askMultipleBtPermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionMap ->
            if (permissionMap.values.contains(false)) {
                error.value = ErrorModel(ErrorType.ErrorBluetoothPermMultiple)
            } else {
                setupBluetooth(activity)
            }
        }

    var bluetoothAdapter = mutableStateOf<BluetoothAdapter?>(null)
    var bluetoothGatt = mutableStateOf<BluetoothGatt?>(null)
    var pairedDevicesList = mutableListOf<BluetoothDevice>()

    fun setupBluetooth(activity: ComponentActivity) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true

            BluetoothRepo.getBluetoothAdapter(activity)?.let { adapter ->
                bluetoothAdapter.value = adapter
                if (!PermissionRepo.hasBluetoothPermission(activity)) {
                    //Bluetooth permission not given
                    askBluetoothPermission()
                    isLoading.value = false
                    return@launch
                }

                if (!adapter.isEnabled) {
                    enableBluetooth()
                }
            } ?: run {
                // Device doesn't support Bluetooth
                error.value = ErrorModel(ErrorType.ErrorBluetoothNotSupported)
            }

            isLoading.value = false
        }
    }

    private fun enableBluetooth() {
        askSingleBtPermissionLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
    }

    private fun askBluetoothPermission() {
        askMultipleBtPermissionLauncher.launch(PermissionRepo.getNeededBluetoothPermissions())
    }


}