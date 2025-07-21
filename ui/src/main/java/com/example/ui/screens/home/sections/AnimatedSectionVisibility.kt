package com.example.ui.screens.home.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnimatedSectionVisibility(visible: Boolean, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
        AnimatedVisibility(
            modifier = modifier,
            visible = visible,
            enter = fadeIn(tween(700)),
            exit = fadeOut(tween(700))

        ) {
            content()
        }

}