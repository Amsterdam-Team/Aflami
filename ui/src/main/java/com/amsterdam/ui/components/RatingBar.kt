package com.amsterdam.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun RatingBar(
    selectedStarIndex: Int? = null,
    onRatingStarChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    starsCount: Int = 10,
    starSize: Dp = 24.dp,
    starsSpacing: Dp = 6.dp,
) {
    val density = LocalDensity.current
    var dialogWidth by remember { mutableStateOf(0.dp) }
    val maxRowWidth = ((starSize.value + starsSpacing.value) * starsCount).dp
    val availableSpace = min(maxRowWidth, dialogWidth)
    val newStarSize = (availableSpace.value / starsCount * 5 / 6).dp
    val newStarSpacing = ((availableSpace.value - newStarSize.value * starsCount) / starsCount).dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged {
                dialogWidth = with(density) { it.width.toDp() }
            }
            .padding(horizontal = 12.dp)
            .requiredSizeIn(maxWidth = dialogWidth),
        horizontalArrangement = Arrangement.spacedBy(newStarSpacing, Alignment.CenterHorizontally)
    ) {
        repeat(starsCount) { index ->
            val starIndex = index + 1
            val isSelected = selectedStarIndex != null && starIndex <= selectedStarIndex

            val translateY = remember { Animatable(0f) }

            LaunchedEffect(key1 = selectedStarIndex) {
                if (isSelected) {
                    delay(index * 50L)
                    translateY.snapTo(10f)
                    translateY.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                    )
                }
            }

            val iconResId = if (selectedStarIndex != null && starIndex <= selectedStarIndex) {
                R.drawable.ic_filled_star
            } else {
                R.drawable.ic_outlined_star
            }

            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = "",
                tint = AppTheme.color.yellowAccent,
                modifier =
                    Modifier
                        .size(newStarSize)
                        .graphicsLayer(translationY = translateY.value)
                        .clickable(
                            onClick = { onRatingStarChanged(starIndex) },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ),
            )
        }
    }
}