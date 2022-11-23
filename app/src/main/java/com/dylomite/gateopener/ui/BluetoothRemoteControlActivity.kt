package com.dylomite.gateopener.ui

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.dylomite.gateopener.R
import com.dylomite.gateopener.model.bluetooth.IBluetoothConnection
import com.dylomite.gateopener.viewmodel.BluetoothConnectionViewModel

class BluetoothRemoteControlActivity : ComponentActivity(), IBaseActivity, IBluetoothConnection {

    private val connectionViewModel by lazy {
        BluetoothConnectionViewModel(app = application, connectionListener = this)
    }

    companion object {
        const val TAG = "BluetoothRemoteControlActivity"
        private const val BLUETOOTH_DEVICE_KEY = "BLUETOOTH_DEVICE_KEY"
        fun getStartIntent(context: Context, device: BluetoothDevice) =
            Intent(context, BluetoothRemoteControlActivity::class.java).apply {
                putExtra(BLUETOOTH_DEVICE_KEY, device)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityContents(
            errorModelState = connectionViewModel.error,
            isLoadingState = connectionViewModel.isLoading
        ) {

        }

        getBluetoothDeviceFromIntent(intent = intent)?.let { device ->
            connectionViewModel.connectToDevice(context = this, device = device)
        }
    }

    private fun getBluetoothDeviceFromIntent(intent: Intent): BluetoothDevice? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(BLUETOOTH_DEVICE_KEY, BluetoothDevice::class.java)
        } else {
            intent.extras?.getParcelable(BLUETOOTH_DEVICE_KEY)
        }
    }

    override fun getBluetoothGatt() = connectionViewModel.bluetoothGatt.value

    override fun onDisconnect() {
        Toast
            .makeText(
                this,
                resources.getString(R.string.device_disconnected),
                Toast.LENGTH_SHORT
            )
            .show()
        finish()
    }

}