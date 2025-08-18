package com.amsterdam.designsystem.components

import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
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
    val density = LocalDensity.current
    var paddingValueDp by remember { mutableStateOf(0.dp) }
    val statusBarPadding = if (isStatusBarTransparent) 0.dp else getStatusBarHeight()
    val navigationBarPadding = if (isNavigationBarTransparent) 0.dp else getNavigationBarHeight()
    val screenHeight = getAvailableScreenHeight(statusBarPadding, navigationBarPadding)
    val headerHeight = getHeaderHeight(
        unneededSpace,
        statusBarPadding,
        navigationBarPadding
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
                    paddingValueDp = getPaddingValue(
                        density,
                        it,
                        screenHeight,
                        headerHeight,
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
    statusBarPadding: Dp,
    navigationBarPadding: Dp
): Dp {
    return unneededSpace + statusBarPadding + navigationBarPadding
}

@Composable
private fun getAvailableScreenHeight(
    statusBarPadding: Dp,
    navigationBarPadding: Dp
): Dp {
    return getScreenHeight() - statusBarPadding - navigationBarPadding
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun getScreenHeight(): Dp {
    return LocalConfiguration.current.screenHeightDp.dp
}

@Composable
private fun getStatusBarHeight(): Dp {
    return with(LocalDensity.current) { WindowInsets.safeDrawing.getTop(this).toDp() }
}

@Composable
private fun getNavigationBarHeight(): Dp {
    return with(LocalDensity.current) { WindowInsets.safeDrawing.getBottom(this).toDp() }
}

private fun getPaddingValue(
    density: Density,
    size: IntSize,
    screenHeight: Dp,
    headerHeight: Dp,
): Dp {
    val containerHeight = with(density) { size.height.toDp() }
    return if (screenHeight > headerHeight + containerHeight) {
        (headerHeight) / 2
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
