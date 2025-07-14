package com.example.designsystem.theme

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
import com.example.designsystem.theme.colors.darkThemeColors
import com.example.designsystem.theme.colors.lightThemeColors
import com.example.designsystem.theme.colors.localAflamiAppColors
import com.example.designsystem.utils.LauncherIcon
import com.example.designsystem.utils.SwitchLauncherIcon

@Composable
fun AflamiTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val theme = if (isDarkTheme) darkThemeColors else lightThemeColors

    val activity = LocalContext.current as? Activity
    val view = LocalView.current

    SwitchLauncherIcon(if (isDarkTheme) LauncherIcon.DARK else LauncherIcon.LIGHT)

    if (activity != null) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            activity.window.navigationBarColor = theme.surface.toArgb()
            WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars =
                !isDarkTheme
        }}
        CompositionLocalProvider(
            localAflamiAppColors provides theme,
            LocalIsDarkTheme provides isDarkTheme
        ) {
            content()
        }
    }

    internal val LocalIsDarkTheme = compositionLocalOf<Boolean> {
        error("LocalIsDarkTheme not provided")
    }