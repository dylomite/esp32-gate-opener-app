package com.dylomite.gateopener.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.dylomite.gateopener.ui.theme.GateOpenerTheme

interface IBaseActivity {

    fun ComponentActivity.activityContents(content: @Composable () -> Unit) {
        setContent {
            GateOpenerTheme {
                content()
                //TODO: Impl loading and error dialog
            }
        }
    }
}