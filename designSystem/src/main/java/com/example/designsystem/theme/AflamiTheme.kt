package com.example.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.example.designsystem.theme.colors.darkThemeColors
import com.example.designsystem.theme.colors.lightThemeColors
import com.example.designsystem.theme.colors.localAflamiAppColors

@Composable
fun AflamiTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val theme = if (isDarkTheme) darkThemeColors else lightThemeColors

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