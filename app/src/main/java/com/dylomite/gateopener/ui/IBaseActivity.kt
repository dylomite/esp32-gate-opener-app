package com.dylomite.gateopener.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.dylomite.gateopener.ui.common.ErrorDialogs
import com.dylomite.gateopener.ui.common.LoadingDialogs
import com.dylomite.gateopener.ui.theme.GateOpenerTheme
import com.dylomite.gateopener.viewmodel.IBaseViewModel

interface IBaseActivity {

    fun ComponentActivity.activityContents(
        viewModelsList: List<IBaseViewModel>,
        content: @Composable () -> Unit
    ) {
        setContent {
            GateOpenerTheme {
                content()
                ErrorDialogs(viewModelsList)
                LoadingDialogs(viewModelsList)
            }
        }
    }
}