package com.dylomite.gateopener.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dylomite.gateopener.ui.theme.GateOpenerTheme
import com.dylomite.gateopener.viewmodel.BluetoothConnectionViewModel
import com.dylomite.gateopener.viewmodel.IBaseViewModel

class MainActivity : ComponentActivity(), IBaseActivity {

    private val btConnViewModel by lazy { BluetoothConnectionViewModel(app = application) }

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
                Greeting("Android")
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GateOpenerTheme {
        Greeting("Android")
    }
}