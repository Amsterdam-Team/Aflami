package com.amsterdam.ui.screens.home.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amsterdam.imageviewer.ui.SafeImageView
import io.sifr.shaded.blurProcessor.BlurEdgeTreatment
import io.sifr.shaded.modifiers.blur

@Composable
fun BoxScope.BlurredMoviePoster(
    posterUrl: String,
    modifier: Modifier = Modifier,
    ) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(424.dp)
    ) {
        SafeImageView(
            model = posterUrl,
            contentDescription = null,
            onLoading = { },
            isClassified = false,
            onError = { },
            modifier = Modifier
                .matchParentSize()
                .blur(
                    radius = 5f,
                    edgeTreatment = BlurEdgeTreatment.UNBOUNDED
                )
                .background(Color(0x80000000))
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xCC000000),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )
    }
}