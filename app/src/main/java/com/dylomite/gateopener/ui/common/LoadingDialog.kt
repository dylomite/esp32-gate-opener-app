package com.dylomite.gateopener.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dylomite.gateopener.R

@Composable
fun LoadingDialog(isLoadingState: MutableState<Boolean>) {
    isLoadingState
        .value
        .takeIf { it }
        ?.let {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false
                ),
                content = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.background(
                            color = Color.White,
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_small))
                        ),
                        content = {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_big))
                            )
                        }
                    )
                }
            )
        }
}