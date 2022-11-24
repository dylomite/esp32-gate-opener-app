package com.dylomite.gateopener

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.delay

object Utils {

    suspend fun MutableState<Boolean>.delayedDismiss(delayMs: Long, onDismiss:()->Unit) {
        delay(delayMs)
        if (this.value) {
            onDismiss()
            this.value = false
        }
    }
}