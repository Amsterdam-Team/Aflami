package com.amsterdam.designsystem.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@SuppressLint("ContextCastToActivity")
@Composable
fun Dialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    isDismissible: Boolean = true,
    scrollable: Boolean = true,
    contentColor: Color = AppTheme.color.surface,
    dialogCornerShape: RoundedCornerShape = RoundedCornerShape(16.dp),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = isDismissible,
            dismissOnClickOutside = isDismissible
        ),
    ) {

        Box(
            modifier = modifier
                .wrapContentSize()
                .surfaceWidthBasedOnDeviceMode(isLandscape)
                .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
                    .background(
                        color = contentColor,
                        shape = dialogCornerShape,
                    ).align(Alignment.Center)

            ) {
                content()
            }
        }
    }
}

@Composable
private fun Modifier.surfaceWidthBasedOnDeviceMode(isLandscape: Boolean) = if (isLandscape) {
    this
        .widthIn(max = 600.dp)
        .heightIn(max = 400.dp)
} else {
    this
        .fillMaxWidth()
        .wrapContentHeight()
}

@Composable
@ThemeAndLocalePreviews
private fun DialogPreview() {
    AflamiTheme {
        Dialog(
            content = {},
            onDismiss = {},
            isDismissible = true,
        )
    }
}