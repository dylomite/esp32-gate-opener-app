package com.dylomite.gateopener.ui

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.dylomite.gateopener.R
import com.dylomite.gateopener.model.bluetooth.IBluetoothConnection
import com.dylomite.gateopener.viewmodel.BluetoothCommunicationViewModel
import com.dylomite.gateopener.viewmodel.BluetoothConnectionViewModel

class BluetoothRemoteControlActivity : ComponentActivity(), IBaseActivity, IBluetoothConnection {

    private val connectionViewModel by lazy {
        BluetoothConnectionViewModel(app = application, connectionListener = this)
    }

    private val communicationViewModel by lazy {
        BluetoothCommunicationViewModel(app = application, connectionListener = this)
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
            errorModelState = connectionViewModel.error,//TODO: Map vm
            isLoadingState = connectionViewModel.isLoading
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                DeviceInfo()
            }
        }

        getBluetoothDeviceFromIntent(intent = intent)?.let { device ->
            connectionViewModel.connectToDevice(context = this, device = device)
        }
    }

    @Composable
    private fun DeviceInfo() {
        val deviceName by connectionViewModel.connectedDeviceName

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.connected_to),
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier,
                text = deviceName ?: getString(R.string.none),
                color = if (deviceName != null) Color.Blue else Color.Red,
                fontWeight = FontWeight.Normal
            )
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