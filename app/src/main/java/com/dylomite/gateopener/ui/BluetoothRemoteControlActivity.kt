package com.dylomite.gateopener.ui

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.dylomite.gateopener.viewmodel.BluetoothConnectionViewModel

class BluetoothRemoteControlActivity : ComponentActivity(), IBaseActivity {

    private val btConnViewModel by lazy {
        BluetoothConnectionViewModel(
            app = application, activity = this
        )
    }

    companion object {
        const val TAG = "MainActivity"
        private const val BLUETOOTH_DEVICE_KEY = "BLUETOOTH_DEVICE_KEY"
        fun getStartIntent(context: Context, device: BluetoothDevice) =
            Intent(context, BluetoothRemoteControlActivity::class.java).apply {
                putExtra(BLUETOOTH_DEVICE_KEY, device)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityContents(
            errorModelState = btConnViewModel.error, isLoadingState = btConnViewModel.isLoading
        ) {

        }

        getBluetoothDeviceFromIntent(intent = intent)?.let { device ->
            btConnViewModel.connectToDevice(context = this, device = device)
        }
    }

    private fun getBluetoothDeviceFromIntent(intent: Intent): BluetoothDevice? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(BLUETOOTH_DEVICE_KEY, BluetoothDevice::class.java)
        } else {
            intent.extras?.getParcelable(BLUETOOTH_DEVICE_KEY)
        }
    }

}