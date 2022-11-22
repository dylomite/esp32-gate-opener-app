package com.dylomite.gateopener.viewmodel

import androidx.compose.runtime.MutableState
import com.dylomite.gateopener.model.error.ErrorModel

interface IBaseViewModel {
    var isLoading: MutableState<Boolean>
    var error: MutableState<ErrorModel?>
}