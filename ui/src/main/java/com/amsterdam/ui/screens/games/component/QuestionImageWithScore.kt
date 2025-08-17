package com.amsterdam.ui.screens.games.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuestionImageWithScore(
    questionImageModel: String,
    blurRadius: Int,
    earnedPoint: Int?,
    isHintEnabled: Boolean,
    modifier: Modifier = Modifier,
    onHintClick: () -> Unit = {}
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        GuessPicture(
            blurRadius = blurRadius.dp,
            points = 10,
            imageUrl = questionImageModel,
            isHintVisible = isHintEnabled,
            onClick = onHintClick,
        )
        AnimatedVisibility(
            visible = earnedPoint != null && earnedPoint != 0,
            enter = expandIn(
                expandFrom = Alignment.TopCenter,
                clip = false,
                animationSpec = tween(1000)
            ) + fadeIn(animationSpec = tween(1000))
        ) {
            if (earnedPoint == null) return@AnimatedVisibility
            GameScoreCircle(
                score = earnedPoint,
                modifier = Modifier.padding(bottom = if (isHintEnabled) 24.dp else 0.dp)
            )
        }
    }
}