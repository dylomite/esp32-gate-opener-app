package com.dylomite.gateopener.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.dylomite.gateopener.model.error.ErrorModel
import com.dylomite.gateopener.ui.common.Alert
import com.dylomite.gateopener.ui.common.LoadingDialog
import com.dylomite.gateopener.ui.theme.GateOpenerTheme

interface IBaseActivity {

    fun ComponentActivity.activityContents(
        errorModelState: MutableState<ErrorModel?>,
        isLoadingState: MutableState<Boolean>,
        content: @Composable () -> Unit
    ) {
        setContent {
            GateOpenerTheme {
                content()
                Alert(errorModelState = errorModelState)
                LoadingDialog(isLoadingState = isLoadingState)
            }
        }
    }
}