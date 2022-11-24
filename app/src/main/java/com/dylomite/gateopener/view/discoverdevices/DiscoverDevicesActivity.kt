package com.dylomite.gateopener.view.discoverdevices

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.dylomite.gateopener.R
import com.dylomite.gateopener.view.IBaseActivity
import com.dylomite.gateopener.view.remotecontrol.RemoteControlActivity
import com.dylomite.gateopener.viewmodel.DevicesListViewModel
import kotlinx.coroutines.launch


class DiscoverDevicesActivity : ComponentActivity(), IBaseActivity {

    private val devicesListViewModel by lazy {
        DevicesListViewModel(
            app = application,
            activity = this
        )
    }

    companion object {
        const val TAG = "DiscoverDevicesActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityContents(viewModelsList = listOf(devicesListViewModel)) {
            PairedDevicesList()
        }

        devicesListViewModel.setupBluetooth(this)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @SuppressLint("MissingPermission")
    @Composable
    private fun PairedDevicesList() {
        val context = LocalContext.current
        val isLoadingDevicesList by devicesListViewModel.isLoadingDevicesList
        val scope = rememberCoroutineScope()
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isLoadingDevicesList,
            onRefresh = { scope.launch { devicesListViewModel.listPairedDevices(context) } }
        )

        Column(modifier = Modifier.pullRefresh(pullRefreshState)) {
            PullRefreshIndicator(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .zIndex(Float.MAX_VALUE),
                refreshing = isLoadingDevicesList,
                state = pullRefreshState
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.padding_mid)),
                text = stringResource(id = R.string.paired_devices_title),
                textAlign = TextAlign.Center,
                fontSize = dimensionResource(id = R.dimen.text_big).value.sp,
                fontWeight = FontWeight.Bold
            )

            if (devicesListViewModel.pairedDevicesList.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(devicesListViewModel.pairedDevicesList) { index, device ->
                        Divider()
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = dimensionResource(id = R.dimen.padding_small),
                                    vertical = dimensionResource(id = R.dimen.padding_mid)
                                )
                                .clickable {
                                    startActivity(
                                        RemoteControlActivity.getStartIntent(
                                            context = context,
                                            device = device
                                        )
                                    )
                                }
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                text = device.name
                            )
                            Text(
                                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small)),
                                fontWeight = FontWeight.Light,
                                text = "[${device.address}]"
                            )
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
