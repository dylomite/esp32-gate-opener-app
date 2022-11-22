package com.dylomite.gateopener.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.dylomite.gateopener.model.error.ErrorModel

class BluetoothConnectionViewModel(app: Application) : AndroidViewModel(app), IBaseViewModel {

    override var isLoading = mutableStateOf(false)
    override var error = mutableStateOf<ErrorModel?>(null)
}