package com.dylomite.gateopener.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.dylomite.gateopener.model.bluetooth.IBluetoothConnection
import com.dylomite.gateopener.model.error.ErrorModel

class BluetoothCommunicationViewModel(
    app: Application,
    private val connectionListener: IBluetoothConnection
) : AndroidViewModel(app), IBaseViewModel {

    companion object {
        const val TAG = "BluetoothCommunicationViewModel"
    }

    override var isLoading = mutableStateOf(false)
    override var error = mutableStateOf<ErrorModel?>(null)

}