package com.amsterdam.ui.screens.onBoarding.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.amsterdam.ui.R
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme

@Composable
fun BoxScope.AnimatedSkipText(
    modifier: Modifier = Modifier, isVisible: Boolean, onClick: () -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current
    AnimatedVisibility(
        modifier = Modifier
            .zIndex(10f)
            .statusBarsPadding()
            .padding(start = 16.dp, top = 16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .align(Alignment.TopStart),
        visible = isVisible,
        enter = slideInHorizontally(
            tween(durationMillis = 1000), initialOffsetX = {
                if (layoutDirection == LayoutDirection.Rtl) it + 1000 else it - 1000
            }),
        exit = slideOutHorizontally(tween(durationMillis = 1000), targetOffsetX = {
            if (layoutDirection == LayoutDirection.Rtl) it + 1000 else it - 1000
        }),
    ) {
        Text(
            modifier = modifier, text = stringResource(
                id = R.string.skip
            ), style = AppTheme.textStyle.label.medium, color = AppTheme.color.primary
        )
    }
}