package com.dylomite.gateopener.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.dylomite.gateopener.R
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
            PairedDevicesList()
        }
    }

    @SuppressLint("MissingPermission")
    @Composable
    private fun PairedDevicesList() {
        val pairedDevicesList = btConnViewModel.pairedDevicesList
        val isLoadingDevicesList by btConnViewModel.isLoadingDevicesList

        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.paired_devices_title),
                textAlign = TextAlign.Center,
                fontSize = dimensionResource(id = R.dimen.text_big).value.sp,
                fontWeight = FontWeight.Bold
            )

            if (isLoadingDevicesList) {
                Row(modifier = Modifier.fillMaxWidth(), Arrangement.Center) {
                    CircularProgressIndicator()
                }
            }

            if (pairedDevicesList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = rememberLazyListState(),
                ) {
                    itemsIndexed(pairedDevicesList) { index, device ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = dimensionResource(id = R.dimen.padding_small),
                                    vertical = dimensionResource(id = R.dimen.padding_mid)
                                )
                                .clickable { /*TODO connect to device*/ }
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                text = device.name
                            )
                            Text(
                                modifier = Modifier.padding(
                                    start = dimensionResource(id = R.dimen.padding_small),
                                    top = dimensionResource(id = R.dimen.padding_small)
                                ),
                                fontWeight = FontWeight.Bold,
                                text = "[${device.address}]"
                            )
                            Divider()
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.padding_small),
                            vertical = dimensionResource(id = R.dimen.padding_mid)
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.error_device_not_paired),
                        textAlign = TextAlign.Center,
                        fontSize = dimensionResource(id = R.dimen.text_mid).value.sp,
                    )
                }
            }
        }
    }

}
