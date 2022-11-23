package com.dylomite.gateopener.ui

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dylomite.gateopener.R
import com.dylomite.gateopener.model.bluetooth.IBluetoothConnection
import com.dylomite.gateopener.ui.theme.Shapes
import com.dylomite.gateopener.ui.theme.appColors
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
                ControlPanel()
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_small),
                    vertical = dimensionResource(id = R.dimen.padding_mid),
                ),
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

    @Composable
    private fun ControlPanel() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PushToActivateButton(
                modifier = Modifier.weight(1f),
                title = getString(R.string.channel_a)
            )
            PushToActivateButton(
                modifier = Modifier.weight(1f),
                title = getString(R.string.channel_b)
            )
        }
    }

    @Composable
    fun PushToActivateButton(modifier: Modifier, title: String) {
        val context = LocalContext.current
        Column(
            modifier = modifier
                .fillMaxWidth(.8f)
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_big),
                    vertical = dimensionResource(id = R.dimen.padding_mid),
                )
                .clip(shape = RoundedCornerShape(30))
                .background(color = appColors().primary)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            Log.d(TAG, "PushToActivateButton: $title PRESSED")
                            this.tryAwaitRelease()
                            Log.d(TAG, "PushToActivateButton: $title RELEASED")
                        },
                        onDoubleTap = { },
                        onLongPress = { },
                        onTap = {}
                    )
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                Text(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_mid)),
                    text = title,
                    color = appColors().onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.text_big).value.sp
                )
            }
        )
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