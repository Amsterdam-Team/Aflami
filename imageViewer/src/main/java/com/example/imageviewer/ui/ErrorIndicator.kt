package com.example.imageviewer.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.imageviewer.R

@Composable
fun ErrorIndicator(
    modifier: Modifier = Modifier,
    tint: Color = Color.Gray,
    size: Dp = 48.dp,
) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
        contentDescription = null,
        tint = tint,
        modifier = modifier.size(size),
    )
}

@Preview
@Composable
private fun ErrorIndicatorPreview() {
    ErrorIndicator()
}