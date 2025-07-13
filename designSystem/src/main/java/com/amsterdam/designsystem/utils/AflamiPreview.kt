package com.amsterdam.designsystem.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "1- Light - English",
    group = "Themes and Locales",
    showBackground = true,
    backgroundColor = 0xFFFAF5F7,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en",
)
@Preview(
    name = "2- Dark - English",
    group = "Themes and Locales",
    showBackground = true,
    backgroundColor = 0xFF0D090B,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "en",
)
@Preview(
    name = "3- Light - Arabic",
    group = "Themes and Locales",
    showBackground = true,
    backgroundColor = 0xFFFAF5F7,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "ar",
)
@Preview(
    name = "4- Dark - Arabic",
    group = "Themes and Locales",
    showBackground = true,
    backgroundColor = 0xFF0D090B,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "ar",
)
annotation class ThemeAndLocalePreviews
