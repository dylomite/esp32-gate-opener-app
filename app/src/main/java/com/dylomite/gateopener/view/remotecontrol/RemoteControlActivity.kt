package com.dylomite.gateopener.view.remotecontrol

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.dylomite.gateopener.R
import com.dylomite.gateopener.bluetooth.IBluetoothConnection
import com.dylomite.gateopener.model.Channel
import com.dylomite.gateopener.model.CharacteristicValue
import com.dylomite.gateopener.view.IBaseActivity
import com.dylomite.gateopener.viewmodel.CommunicationViewModel
import com.dylomite.gateopener.viewmodel.ConnectionViewModel

class RemoteControlActivity : ComponentActivity(), IBaseActivity, IBluetoothConnection {

    private val connectionViewModel by lazy {
        ConnectionViewModel(app = application, connectionListener = this)
    }

    private val communicationViewModel by lazy {
        CommunicationViewModel(app = application, connectionListener = this)
    }

    companion object {
        const val TAG = "RemoteControlActivity"
        private const val BLUETOOTH_DEVICE_KEY = "BLUETOOTH_DEVICE_KEY"
        fun getStartIntent(context: Context, device: BluetoothDevice) =
            Intent(context, RemoteControlActivity::class.java).apply {
                putExtra(BLUETOOTH_DEVICE_KEY, device)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityContents(viewModelsList = listOf(connectionViewModel, communicationViewModel)) {
            Column(modifier = Modifier.fillMaxSize()) {
                DeviceInfo()
                ControlPanel()
            }
        }

        getBluetoothDeviceFromIntent(intent = intent)?.let { device ->
            connectionViewModel.connectToDevice(context = this, device = device)
        }
    }

    override fun onDestroy() {
        connectionViewModel.disconnectDeviceAndReset(connectionViewModel.bluetoothGatt.value)
        super.onDestroy()
    }

    @Composable
    private fun DeviceInfo() {
        val deviceName by connectionViewModel.connectedDeviceName

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_small),
                    vertical = dimensionResource(id = R.dimen.padding_mid),
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.connected_to), fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier,
                text = deviceName ?: getString(R.string.none),
                color = if (deviceName != null) Color.Blue else Color.Red,
                fontWeight = FontWeight.Normal
            )
        }
    }

    @Composable
    private fun ControlPanel() {
        val context = LocalContext.current
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PushToActivateButton(
                modifier = Modifier.weight(1f),
                title = getString(R.string.channel_a),
                channel = Channel.ChannelA,
                onPressEvent = { channel, isPressed ->
                    communicationViewModel.sendSignal(
                        context = context,
                        channel = channel,
                        characteristicValue = if (isPressed) CharacteristicValue.High else CharacteristicValue.Low
                    )
                }
            )
            PushToActivateButton(
                modifier = Modifier.weight(1f),
                title = getString(R.string.channel_b),
                channel = Channel.ChannelB,
                onPressEvent = { channel, isPressed ->
                    communicationViewModel.sendSignal(
                        context = context,
                        channel = channel,
                        characteristicValue = if (isPressed) CharacteristicValue.High else CharacteristicValue.Low
                    )
                }
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
        Toast.makeText(
            this, resources.getString(R.string.device_disconnected), Toast.LENGTH_SHORT
        ).show()
        finish()
    }

}