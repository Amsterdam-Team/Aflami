package com.amsterdam.designsystem.components

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
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun CenterOfScreenContainer(
    unneededSpace: Dp,
    modifier: Modifier = Modifier,
    isStatusBarTransparent: Boolean = false,
    isNavigationBarTransparent: Boolean = false,
    content: @Composable () -> Unit,
) {
    var paddingValueDp by remember { mutableStateOf(0.dp) }
    val screenHeight = getAvailableScreenHeight(isStatusBarTransparent, isNavigationBarTransparent)
    val statusBarHeight = getStatusBarHeight()
    val navigationBarHeight = getNavigationBarHeight()
    val headerHeight = getHeaderHeight(
        unneededSpace,
        isStatusBarTransparent,
        isNavigationBarTransparent,
        statusBarHeight,
        navigationBarHeight
    )
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
                        it,
                        screenHeight,
                        headerHeight,
                        if (isStatusBarTransparent) statusBarHeight else 0.dp,
                        if (isNavigationBarTransparent) navigationBarHeight else 0.dp,
                    )
                }
        ) {
            content()
        }
    }
}

@Composable
private fun getHeaderHeight(
    unneededSpace: Dp,
    isStatusBarTransparent: Boolean,
    isNavigationBarTransparent: Boolean,
    statusBarHeight: Dp,
    navigationBarHeight: Dp
): Dp {
    return unneededSpace + when {
        isStatusBarTransparent && isNavigationBarTransparent -> statusBarHeight + navigationBarHeight
        isStatusBarTransparent -> statusBarHeight
        isNavigationBarTransparent -> navigationBarHeight
        else -> 0.dp
    }
}

@Composable
private fun getAvailableScreenHeight(
    isStatusBarTransparent: Boolean,
    isNavigationBarTransparent: Boolean
): Dp {
    val fullScreenHeight = getScreenHeight()
    val statusBarHeight = if (isStatusBarTransparent) 0.dp else getStatusBarHeight()
    val navigationBarHeight = if (isNavigationBarTransparent) 0.dp else getNavigationBarHeight()

    val screenHeight = remember { fullScreenHeight - statusBarHeight - navigationBarHeight }
    return screenHeight
}

@Composable
private fun getScreenHeight(): Dp {
    return LocalContext.current.resources.displayMetrics.heightPixels.dp
}

@Composable
private fun getStatusBarHeight(): Dp {
    return with(LocalDensity.current) { WindowInsets.safeDrawing.getTop(this).toDp() }
}

@Composable
private fun getNavigationBarHeight(): Dp {
    return with(LocalDensity.current) { WindowInsets.safeDrawing.getBottom(this).toDp() }
}

private fun getContainerSize(
    size: IntSize,
    screenHeight: Dp,
    headerHeight: Dp,
    statusBarHeight: Dp = 0.dp,
    navigationBarHeight: Dp = 0.dp,
): Dp {
    val containerHeight = size.height.dp
    return if (screenHeight > headerHeight * 2 + containerHeight) {
        (statusBarHeight + navigationBarHeight + headerHeight) / 2
    } else {
        0.dp
    }
}

@ThemeAndLocalePreviews
@Composable
private fun CenterOfScreenContainerPreview() {
    AflamiTheme {
        CenterOfScreenContainer(unneededSpace = 5.dp) {
        }
    }
}
