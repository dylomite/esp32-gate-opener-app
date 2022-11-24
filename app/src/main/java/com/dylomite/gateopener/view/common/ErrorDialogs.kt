package com.dylomite.gateopener.view.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.dylomite.gateopener.R
import com.dylomite.gateopener.viewmodel.IBaseViewModel

@Composable
fun ErrorDialogs(viewModelsList: List<IBaseViewModel>) {

    viewModelsList.forEach { viewModel ->
        viewModel.error.value?.let { errorModel ->

            AlertDialog(
                onDismissRequest = { errorModel.onDismissButtonPressed() },
                title = { Text(stringResource(id = R.string.error_generic_title)) },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        if (errorModel.showDiscardButton) {
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = dimensionResource(id = R.dimen.padding_small)),
                                content = { Text(stringResource(id = R.string.discard)) },
                                onClick = {
                                    errorModel.onDiscardButtonPressed.invoke()
                                    viewModel.error.value = null
                                }
                            )
                        }
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = dimensionResource(id = R.dimen.padding_small)),
                            content = { Text(stringResource(id = R.string.ok)) },
                            onClick = {
                                errorModel.onActionButtonPressed.invoke()
                                viewModel.error.value = null
                            }
                        )
                    }
                },
                text = { Text(errorModel.getMessage(LocalContext.current)) },
            )

        }
    }
}
