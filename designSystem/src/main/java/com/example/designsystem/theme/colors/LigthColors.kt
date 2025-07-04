package com.example.designsystem.theme.colors

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val lightThemeColors = AflamiColorScheme(
    primary = Color(0xFFF564A9),
    secondary = Color(0xFF7D1C4A),
    primaryVariant = Color(0xFFFAE6EF),
    title = Color(0xDE1f1f1f),
    body = Color(0x991f1f1f),
    hint = Color(0x611f1f1f),
    stroke = Color(0x141f1f1f),
    surface = Color(0xFFFAF5F7),
    surfaceHigh = Color(0xFFFFFFFF),
    onPrimary = Color(0xdeffffff),
    onPrimaryBody = Color(0x80ffffff),
    onPrimaryHint = Color(0x14ffffff),
    disable = Color(0xFFE1E4E5),
    iconBackGround = Color(0xb30d090b),
    blurOverlay = Color(0x80ffffff),
    onPrimaryButton = Color(0x70ffffff),
    redAccent = Color(0xFFD94C56),
    redVariant = Color(0xFFF2DFE0),
    yellowAccent = Color(0xFFFCB032),
    greenAccent = Color(0xFF429946),
    greenVariant = Color(0xFFDFF2E0),
    darkBlue = Color(0xFF0C57C8),
    blueAccent = Color(0xFF2BA3D9),
    blueCard = Color(0x3d8dd3f2),
    navyCard = Color(0x3d91a9fa),
    yellowCard = Color(0x3dfad291),
    backgroundCircles = Color(0x3dd85895),
    profileOverlay = Color(0x80faf5f7),
    overlayGradient = listOf(Color(0x00faf5f7), Color(0xFFFAF5F7)),
    streakGradient = listOf(Color(0xFFD85895), Color(0x52d85895)),
    pointsOverlayGradient = listOf(Color(0xFFD02C7A), Color(0xFF7D1C4A)),
    borderLinearGradient = listOf(Color(0x14FFFFFF), Color(0x3DFFFFFF)),
    successSnackBarShadow = Color(0x1F429946),
    failureSnackBarShadow = Color(0x1FBF434C),
    softBlue = Color(0x80EFF9FE),
    worldTourGradient = listOf(Color(0xFFD85895), Color(0xFF803559)),
    findByActorGradient = listOf(Color(0xFF53ABF9), Color(0xFF336490)),
    guessCardGradient = listOf(Color(0x1FD85895), Color(0xFFFAF5F7)),
    primaryEnd = Color(0xFF973A66),
)

internal val localAflamiAppColors = staticCompositionLocalOf { lightThemeColors }