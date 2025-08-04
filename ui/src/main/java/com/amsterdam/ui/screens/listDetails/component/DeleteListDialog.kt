package com.amsterdam.ui.screens.listDetails.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ButtonDefaults
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.R

@Composable
internal fun DeleteListDialog(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(
        onDismiss = onDismiss,
        isDismissible = true,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DialogHeader(
                titleResource = R.string.delete_list,
                onDismiss = onDismiss
            )

            Image(
                painter = painterResource(R.drawable.alert),
                contentDescription = stringResource(R.string.delete_list),
                modifier = Modifier.height(100.dp),
                contentScale = ContentScale.FillHeight
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(R.string.confirm_delete),
                style = AppTheme.textStyle.title.small,
                color = AppTheme.color.body,
                textAlign = TextAlign.Center
            )

            ConfirmButton(
                modifier = Modifier.padding(top = 24.dp),
                title = stringResource(R.string.delete),
                onClick = onConfirm,
                isEnabled = true,
                isLoading = isLoading,
                isNegative = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.color.redVariant
                )
            )
        }
    }
}

@Composable
private fun DialogHeader(
    titleResource: Int,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(titleResource),
            style = AppTheme.textStyle.title.large,
            color = AppTheme.color.title
        )
        IconButton(
            painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_cancel),
            contentDescription = stringResource(R.string.cancel),
            onClick = { onDismiss() },
            tint = AppTheme.color.title,
        )
    }
}

@Preview
@Composable
fun DeleteListDialogPreview() {
    AflamiTheme {
        DeleteListDialog(
            isLoading = false,
            onDismiss = {},
            onConfirm = {}
        )
    }
}