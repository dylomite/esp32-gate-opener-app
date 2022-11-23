package com.dylomite.gateopener.model

import java.util.*

sealed class Channel {
    object ChannelA : Channel()
    object ChannelB : Channel()
}
