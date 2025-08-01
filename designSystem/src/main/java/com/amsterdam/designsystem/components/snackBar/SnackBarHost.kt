package com.amsterdam.designsystem.components.snackBar

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BoxScope.SnackBarHost(
    modifier: Modifier = Modifier
) {
    var currentSnackBar by remember { mutableStateOf<SnackBarData?>(null) }
    var showSnackBar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        SnackBarManager.snackBarFlow.collectLatest { snackBarData ->
            currentSnackBar = snackBarData
            showSnackBar = true

            delay(snackBarData.duration)
            showSnackBar = false

            delay(300)
            currentSnackBar = null
        }
    }

    AnimatedVisibility(
        visible = showSnackBar ,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(300, easing = EaseOutQuart)
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(300, easing = EaseInQuart)
        ),
        modifier = modifier
    ) {
        currentSnackBar?.let { snackBar ->
            SnackBar(
                message = snackBar.message,
                status = snackBar.status,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}