package com.dylomite.gateopener.view.remotecontrol

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dylomite.gateopener.R
import com.dylomite.gateopener.model.Channel
import com.dylomite.gateopener.view.theme.appColors

@Composable
fun PushToActivateButton(
    modifier: Modifier,
    title: String,
    channel: Channel,
    onPressEvent: (channel: Channel, isPushed: Boolean) -> Unit
) {
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
                        onPressEvent(channel, true)
                        this.tryAwaitRelease()
                        onPressEvent(channel, false)
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
