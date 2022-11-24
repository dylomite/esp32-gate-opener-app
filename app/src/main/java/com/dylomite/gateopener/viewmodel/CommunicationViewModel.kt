package com.dylomite.gateopener.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.dylomite.gateopener.bluetooth.IBluetoothConnection
import com.dylomite.gateopener.model.Channel
import com.dylomite.gateopener.model.CharacteristicValue
import com.dylomite.gateopener.model.error.ErrorModel
import com.dylomite.gateopener.model.error.ErrorType
import com.dylomite.gateopener.repo.BluetoothRepo

class CommunicationViewModel(
    app: Application,
    private val connectionListener: IBluetoothConnection
) : AndroidViewModel(app), IBaseViewModel {

    companion object {
        const val TAG = "CommunicationViewModel"
    }

    override var isLoading = mutableStateOf(false)
    override var error = mutableStateOf<ErrorModel?>(null)

    @SuppressLint("MissingPermission")
    fun sendSignal(
        context: Context,
        channel: Channel,
        characteristicValue: CharacteristicValue
    ) {
        connectionListener.getBluetoothGatt()?.let { gatt ->
            gatt.getService(BluetoothRepo.getServiceUUID())?.let { service ->
                service
                    .getCharacteristic(BluetoothRepo.getChannelUUID(channel))
                    ?.let { characteristic ->
                        //Communication
                        characteristic.value = characteristicValue.value
                        gatt.writeCharacteristic(characteristic)
                    }
                    ?: run { error.value = ErrorModel(ErrorType.ErrorGattCharacteristicNull) }
            } ?: run { error.value = ErrorModel(ErrorType.ErrorGattServiceNull) }
        } ?: run {
            error.value = ErrorModel(
                errorType = ErrorType.ErrorDeviceDisconnected,
                onActionButtonPressed = { connectionListener.onDisconnect() }
            )
        }
    }
}