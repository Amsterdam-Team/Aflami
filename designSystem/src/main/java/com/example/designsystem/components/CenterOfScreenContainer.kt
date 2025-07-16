package com.example.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun CenterOfScreenContainer(
    unneededSpace: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var paddingValueDp by remember { mutableStateOf(0.dp) }
    var containerHeight by remember { mutableStateOf(0.dp) }
    val screenHeight = getAppScreenHeight()
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = paddingValueDp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .onSizeChanged {
                    paddingValueDp = getContainerSize(
                        containerHeight,
                        it,
                        paddingValueDp,
                        screenHeight,
                        unneededSpace
                    )
                },
        ) {
            content()
        }
    }
}

@Composable
private fun getAppScreenHeight(): Dp {
    val fullScreenHeight =
        LocalContext.current.resources.displayMetrics.heightPixels.dp
    val statusBarHeight =
        with(LocalDensity.current) { WindowInsets.safeDrawing.getTop(this).toDp() }
    val navigationBarHeight =
        with(LocalDensity.current) { WindowInsets.safeDrawing.getBottom(this).toDp() }

    val screenHeight = remember { fullScreenHeight - statusBarHeight - navigationBarHeight }
    return screenHeight
}

private fun getContainerSize(
    containerHeight: Dp,
    size: IntSize,
    paddingValueDp: Dp,
    screenHeight: Dp,
    headerHeight: Dp
): Dp {
    var containerHeight1 = containerHeight
    var paddingValueDp1 = paddingValueDp
    containerHeight1 = size.height.dp
    paddingValueDp1 =
        if (screenHeight > headerHeight * 2 + containerHeight1)
            headerHeight / 2
        else 0.dp
    return paddingValueDp1
}
@ThemeAndLocalePreviews
@Composable
private fun CenterOfScreenContainerPreview(){
    AflamiTheme {
        CenterOfScreenContainer(unneededSpace = 5.dp) {
        }

    }
}