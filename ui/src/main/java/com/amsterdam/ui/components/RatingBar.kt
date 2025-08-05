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
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
    starsSpacing: Dp = 12.dp,
    useEqualSpacing: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp),
        horizontalArrangement = if (useEqualSpacing) {
            Arrangement.Start
        } else {
            Arrangement.spacedBy(starsSpacing, Alignment.CenterHorizontally)
        }
    ) {
        repeat(starsCount) { index ->
            val starIndex = index + 1
            val isSelected = selectedStarIndex != null && starIndex <= selectedStarIndex

            val translateY = remember { Animatable(0f) }

            LaunchedEffect(key1 = selectedStarIndex) {
                if (isSelected) {
                    delay(index * 50L) // delay per star
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
                        .size(starSize)
                        .then(if (useEqualSpacing) Modifier.weight(1f) else Modifier)
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