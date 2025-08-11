package com.amsterdam.ui.components.guessGame

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.sharedGame.TimerUiState

@Composable
fun TimerComponent(
    timerUiState: TimerUiState,
    modifier: Modifier = Modifier
) {
    val greenColor = AppTheme.color.greenAccent
    val redColor = AppTheme.color.redAccent
    val targetColor by remember(timerUiState.currentTimerColor) {
        derivedStateOf {
            when (timerUiState.currentTimerColor) {
                TimerUiState.TimerColor.GREEN -> greenColor
                TimerUiState.TimerColor.RED -> redColor
            }
        }
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = modifier.size(40.dp).background(AppTheme.color.primaryVariant, CircleShape)
    ) {
        Text(
            text = stringResource(R.string.second, timerUiState.currentTimerCount),
            style = AppTheme.textStyle.label.small,
            color = animatedColor,
            modifier = Modifier
                .align(Alignment.Center)
                .zIndex(1f)
        )

        Canvas(modifier = Modifier.size(40.dp).zIndex(10f)) {
            val strokeWidth = 3.dp.toPx()
            drawArc(
                color = animatedColor,
                startAngle = -90f,
                sweepAngle = timerUiState.progress * 360,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}

