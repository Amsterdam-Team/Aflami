package com.amsterdam.ui.screens.home.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.imageviewer.classification.SafetyLevel
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.application.LocalRestrictionLevel
import com.amsterdam.ui.utils.toSafetyLevel
import com.amsterdam.ui.utils.topGradient
import io.sifr.shaded.blurProcessor.BlurEdgeTreatment
import io.sifr.shaded.modifiers.blur

@Composable
fun BlurredMediaPoster(
    posterUrl: String,
    modifier: Modifier = Modifier
) {
    val safetyLevel = LocalRestrictionLevel.current.toSafetyLevel()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(424.dp)
    ) {
        SafeImageView(
            model = posterUrl,
            contentDescription = null,
            onLoading = { },
            safetyLevel = safetyLevel,
            onError = { },
            modifier = Modifier
                .matchParentSize()
                .blur(
                    radius = 8f,
                    edgeTreatment = BlurEdgeTreatment.UNBOUNDED
                )
                .background(Color(0x80000000))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
                .topGradient()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AppTheme.color.blurOverlay,
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )
    }
}