package com.example.designsystem.components.customSnackBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.designsystem.utils.dropShadow

@Composable
fun BoxScope.SnackBar(message: String, status: SnackBarStatus) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(19.dp)
            .fillMaxWidth()
            .dropShadow(
                blur = 8.dp,
                shape = shape,
                color = status.dropShadowColor(),
            )
            .padding(1.dp)
            .clip(shape = shape)
            .clipToBounds()
            .background(AppTheme.color.surfaceHigh, shape = shape)
            .border(1.dp, AppTheme.color.stroke, shape)
    ) {
        Row(
            modifier = Modifier
                .defaultMinSize(minHeight = 56.dp)
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(status.icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(status.iconTintColor()),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = message,
                style = AppTheme.textStyle.body.medium,
                color = AppTheme.color.body,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@ThemeAndLocalePreviews()
@Composable
private fun SnackBarSuccessPreview() {
    AflamiTheme {
        Box {
            SnackBar(
                stringResource(R.string.list_added_success_message),
                SnackBarStatus.Success
            )
        }
    }
}

@ThemeAndLocalePreviews()
@Composable
private fun SnackBarFailurePreview() {
    AflamiTheme {
        Box {
            SnackBar(
                stringResource(R.string.general_error_message),
                SnackBarStatus.Failure
            )
        }
    }
}
