package com.amsterdam.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.amsterdam.designsystem.theme.colors.LocalAflamiAppColors
import com.amsterdam.designsystem.theme.colors.darkThemeColors
import com.amsterdam.designsystem.theme.colors.lightThemeColors

@Composable
fun AflamiTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val theme = if (isDarkTheme) darkThemeColors else lightThemeColors

    val activity = LocalContext.current as? Activity
    val view = LocalView.current

    if (activity != null) {
        activity.window.navigationBarColor = theme.surface.toArgb()
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars =
            !isDarkTheme
    }
    CompositionLocalProvider(
        LocalAflamiAppColors provides theme,
        LocalIsDarkTheme provides isDarkTheme,
    ) {
        content()
    }
}

val LocalIsDarkTheme =
    compositionLocalOf<Boolean> {
        error("LocalIsDarkTheme not provided")
    }
