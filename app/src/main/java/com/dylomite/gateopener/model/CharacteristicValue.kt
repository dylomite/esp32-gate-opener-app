package com.dylomite.gateopener.model

sealed class CharacteristicValue(val value: ByteArray) {
    object Low : CharacteristicValue("SIG_OFF".toByteArray())
    object High : CharacteristicValue("SIG_ON".toByteArray())
}
