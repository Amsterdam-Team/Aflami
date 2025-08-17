package com.amsterdam.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.designsystem.utils.modifierExtensions.shimmerEffect

@Composable
fun RoundedShimmerPlaceholder(
    height: Dp,
    width: Dp,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(height = height, width = width)
            .clip(RoundedCornerShape(cornerRadius))
            .shimmerEffect(),
        contentAlignment = Alignment.Center
    ){
        content()
    }
}

@ThemeAndLocalePreviews
@Composable
private fun RoundedShimmerPlaceholderPreview() {
    RoundedShimmerPlaceholder(
        height = 244.dp,
        width = 300.dp,
        cornerRadius = 24.dp,
    )
}