package com.dylomite.gateopener.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.btsample.model.error.ErrorModel

class BluetoothConnectionViewModel(app: Application) : AndroidViewModel(app) {

    val isLoading = MutableLiveData(false)
    val error = MutableLiveData<ErrorModel?>(null)
}