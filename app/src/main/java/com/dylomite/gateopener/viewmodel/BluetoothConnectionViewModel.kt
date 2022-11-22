package com.dylomite.gateopener.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dylomite.gateopener.model.bluetooth.BluetoothCallbacks
import com.dylomite.gateopener.model.error.ErrorModel
import com.dylomite.gateopener.model.error.ErrorType
import com.dylomite.gateopener.repo.BluetoothRepo
import com.dylomite.gateopener.repo.BluetoothRepo.isBonded
import com.dylomite.gateopener.repo.PermissionRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    @SuppressLint("MissingPermission")
    private val gattCallbacks = BluetoothCallbacks(
        onConnect = { gatt ->
            gatt.discoverServices()
            Toast.makeText(activity, "Connected!", Toast.LENGTH_SHORT).show()
        },
        onDisconnect = { gatt -> disconnectDeviceAndReset(gatt) },
    )
    private var bluetoothAdapter = mutableStateOf<BluetoothAdapter?>(null)
    var bluetoothGatt = mutableStateOf<BluetoothGatt?>(null)

    var pairedDevicesList = mutableStateListOf<BluetoothDevice>()
    var isLoadingDevicesList = mutableStateOf(false)

    private fun enableBluetooth() =
        askSingleBtPermissionLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))

    private fun askBluetoothPermission() =
        askMultipleBtPermissionLauncher.launch(PermissionRepo.getNeededBluetoothPermissions())

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
                } else {
                    listPairedDevices()
                }
            } ?: run {
                // Device doesn't support Bluetooth
                error.value = ErrorModel(ErrorType.ErrorBluetoothNotSupported)
            }

            isLoading.value = false
        }
    }

    @SuppressLint("MissingPermission")
    fun listPairedDevices() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoadingDevicesList.value = true
            bluetoothAdapter.value?.let { adapter ->
                if (!adapter.isEnabled) {
                    enableBluetooth()
                } else {
                    pairedDevicesList = mutableStateListOf()
                    // Get paired devices.
                    val pairedDevices = adapter.bondedDevices
                    if (pairedDevices.size > 0) {
                        pairedDevicesList.addAll(pairedDevices.toList())
                    } else {
                        error.value = ErrorModel(ErrorType.ErrorNoPairedDevices)
                    }
                }
            }
            isLoadingDevicesList.value = false
        }
    }

    /**
     * NOTES:
     *  - Remain on the main thread for the connection part
     *  - Add BluetoothDevice.TRANSPORT_LE param to avoid error status 133!
     *    See: https://github.com/android/connectivity-samples/issues/18
     */
    @SuppressLint("MissingPermission")
    fun connectToDevice(context: Context, device: BluetoothDevice) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true
            if (device.isBonded()) {
                runBlocking(Dispatchers.Main) {
                    bluetoothGatt.value = device
                        .connectGatt(context, true, gattCallbacks, BluetoothDevice.TRANSPORT_LE)
                        .also { gatt -> gatt.connect() }
                }
            } else {
                error.value = ErrorModel(ErrorType.ErrorDeviceNotBonded)
            }
            isLoading.value = false//TODO: Impl dismiss after time
        }
    }

    @SuppressLint("MissingPermission")
    fun disconnectDeviceAndReset(bthGatt: BluetoothGatt? = bluetoothGatt.value) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true
            bthGatt?.let { gatt ->
                gatt.disconnect()
                gatt.close()
            }
            bluetoothAdapter.value = null
            isLoadingDevicesList.value = false
            pairedDevicesList = mutableStateListOf()
            bluetoothGatt.value = null
            isLoading.value = false
        }
    }

}