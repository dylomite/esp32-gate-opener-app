package com.dylomite.gateopener.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dylomite.gateopener.bluetooth.BluetoothDeviceCallbacks
import com.dylomite.gateopener.bluetooth.IBluetoothConnection
import com.dylomite.gateopener.model.error.ErrorModel
import com.dylomite.gateopener.model.error.ErrorType
import com.dylomite.gateopener.repo.BluetoothRepo.isBonded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ConnectionViewModel(
    app: Application,
    private val connectionListener: IBluetoothConnection
) : AndroidViewModel(app), IBaseViewModel {

    companion object {
        const val TAG = "ConnectionViewModel"
    }

    override var isLoading = mutableStateOf(false)
    override var error = mutableStateOf<ErrorModel?>(null)

    @SuppressLint("MissingPermission")
    private val gattCallbacks = BluetoothDeviceCallbacks(
        onConnect = { gatt ->
            gatt.discoverServices()
            connectedDeviceName.value = gatt.device.name
        },
        onDisconnect = {
            connectionListener.onDisconnect()
        },
    )
    private var bluetoothAdapter = mutableStateOf<BluetoothAdapter?>(null)
    val bluetoothGatt = mutableStateOf<BluetoothGatt?>(null)
    val connectedDeviceName = mutableStateOf<String?>(null)

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
                    bluetoothGatt.value = device.connectGatt(
                        context,
                        true,
                        gattCallbacks,
                        BluetoothDevice.TRANSPORT_LE
                    ).also { gatt -> gatt.connect() }
                }
            } else {
                error.value = ErrorModel(ErrorType.ErrorDeviceNotBonded)
            }
            isLoading.value = false//TODO: Impl dismiss after time
        }
    }

    @SuppressLint("MissingPermission")
    fun disconnectDeviceAndReset(bthGatt: BluetoothGatt?) {
        bthGatt?.let { gatt ->
            gatt.disconnect()
            gatt.close()
        }
        bluetoothAdapter.value = null
        bluetoothGatt.value = null
        connectedDeviceName.value = null
        isLoading.value = false
    }

}