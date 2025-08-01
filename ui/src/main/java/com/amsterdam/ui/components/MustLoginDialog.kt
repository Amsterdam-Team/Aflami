package com.amsterdam.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.OutlinedButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.designsystem.utils.modifierExtensions.dropShadow
import com.amsterdam.ui.R

@Composable
fun MustLoginDialog(
    title: String,
    onDismiss: () -> Unit,
    onClickLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
            ),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    AppTheme.color.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = AppTheme.textStyle.title.large,
                    color = AppTheme.color.title
                )
                IconButton(
                    painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_cancel),
                    contentDescription = null,
                    onClick = {
                        onDismiss()
                    },
                    tint = AppTheme.color.title,
                )
            }

            Image(
                painter = painterResource(R.drawable.img_empty_user_pic),
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .dropShadow(
                        blur = 12.dp,
                        shape = RoundedCornerShape(24.dp),
                        color = AppTheme.color.droppedShadowColor
                    )
                    .clip(shape = RoundedCornerShape(24.dp))
                    .border(
                        width = 1.dp, AppTheme.color.stroke,
                        shape = RoundedCornerShape(24.dp)
                    )

            )
            Text(
                modifier = Modifier.padding(vertical = 12.dp),
                text = stringResource(R.string.login_message),
                style = AppTheme.textStyle.body.small,
                color = AppTheme.color.body,
                textAlign = TextAlign.Center

            )
            OutlinedButton(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                title = stringResource(R.string.login),
                onClick = { onClickLogin() },
                isEnabled = true,
                isLoading = false,
                isNegative = false,
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
@ThemeAndLocalePreviews
fun CustomDialogPreview() {
    AflamiTheme {
        MustLoginDialog(
            onDismiss = { },
            title = "Rate",
            onClickLogin = { }
        )

    }
}