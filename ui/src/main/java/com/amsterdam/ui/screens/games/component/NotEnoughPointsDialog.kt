package com.amsterdam.ui.screens.games.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R

@Composable
fun NotEnoughPointsDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    ) {
    Dialog(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.not_enough_points),
                    style = AppTheme.textStyle.title.medium,
                    color = AppTheme.color.title,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    painter = painterResource(id = com.amsterdam.designsystem.R.drawable.ic_cancel),
                    contentDescription = stringResource(id = R.string.cd_cancel),
                    onClick = onDismiss,
                    tint = AppTheme.color.title
                )
            }

            Text(
                text = stringResource(R.string.not_enough_points_description),
                style = AppTheme.textStyle.body.medium,
                color = AppTheme.color.body,
            )

            ConfirmButton(
                title = stringResource(R.string.ok),
                onClick = onConfirm,
                isEnabled = true,
                isLoading = false,
                isNegative = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@ThemeAndLocalePreviews
@Composable
private fun NotEnoughPointsDialogPreview() {
    AflamiTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.color.surface.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppTheme.color.surface)
                    .padding(16.dp)
            ) {
                NotEnoughPointsDialog(onConfirm = {}, onDismiss = {})
            }
        }
    }
}