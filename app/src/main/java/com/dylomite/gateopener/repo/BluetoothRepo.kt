package com.dylomite.gateopener.repo

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.dylomite.gateopener.model.Channel
import java.util.*

@SuppressLint("MissingPermission")
object BluetoothRepo {

    private const val TAG = "BluetoothRepo"

    //UUIDs
    private val SERVICE_UUID: UUID = UUID.fromString("bc5d00ca-badf-4244-ae11-4a8f73fd409f")
    private val CHANNEL_A_UUID: UUID = UUID.fromString("1dd23185-5d48-457c-a5e4-0f21afed2e58")
    private val CHANNEL_B_UUID: UUID = UUID.fromString("04889dda-9528-45fb-ac11-f522997afb0b")

    fun BluetoothDevice.isBonded() = this.bondState == BluetoothDevice.BOND_BONDED

    private fun getBluetoothManager(context: Context): BluetoothManager? {
        return try {
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        } catch (e: Exception) {
            Log.e(TAG, "getBluetoothManager: ", e)
            null
        }
    }

    fun getBluetoothAdapter(context: Context) = getBluetoothManager(context)?.adapter

    fun getServiceUUID() = SERVICE_UUID

    fun getChannelUUID(channel: Channel) = when(channel){
        Channel.ChannelA -> CHANNEL_A_UUID
        Channel.ChannelB -> CHANNEL_B_UUID
    }
}