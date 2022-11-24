package com.dylomite.gateopener.view

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.dylomite.gateopener.view.common.ErrorDialogs
import com.dylomite.gateopener.view.common.LoadingDialogs
import com.dylomite.gateopener.view.theme.GateOpenerTheme
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