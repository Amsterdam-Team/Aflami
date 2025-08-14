package com.amsterdam.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int,
    selectedColor: Color = AppTheme.color.onPrimary,
    unselectedColor: Color = AppTheme.color.onPrimaryHint,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        repeat(pageCount) { page ->
            val isSelected = page <= currentPage
            val animatedColor by animateColorAsState(
                targetValue = if (isSelected) selectedColor else unselectedColor,
                animationSpec = tween(durationMillis = 300),
                label = "PageIndicatorColorAnimation"
            )

            Box(
                modifier = Modifier
                    .height(6.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(100.dp))
                    .background(animatedColor)
                    .border(
                        width = 1.dp,
                        color = AppTheme.color.stroke,
                        shape = CircleShape,
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun PageIndicatorPreview() {
    AflamiTheme {
        PageIndicator(
            pageCount = 4,
            currentPage = 1,
        )
    }
}