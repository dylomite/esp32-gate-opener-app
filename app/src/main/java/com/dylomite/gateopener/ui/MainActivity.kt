package com.dylomite.gateopener.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.dylomite.gateopener.viewmodel.BluetoothConnectionViewModel

class MainActivity : ComponentActivity(), IBaseActivity {

    private val btConnViewModel by lazy {
        BluetoothConnectionViewModel(
            app = application,
            activity = this
        )
    }

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityContents(
            errorModelState = btConnViewModel.error,
            isLoadingState = btConnViewModel.isLoading
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                Text("Android")
            }
        }
    }

}
