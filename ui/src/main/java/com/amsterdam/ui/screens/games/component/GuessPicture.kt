package com.amsterdam.ui.screens.games.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.imageviewer.classification.SafetyLevel
import com.amsterdam.imageviewer.ui.SafeImageView
import io.sifr.shaded.blurProcessor.BlurEdgeTreatment
import io.sifr.shaded.modifiers.blur


@Composable
fun GuessPicture(
    blurRadius: Dp,
    points: Int,
    imageUrl: String,
    isHintVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GuessCard(
        points = points,
        modifier = modifier,
        isHintVisible = isHintVisible,
        onClick = onClick,
    ) {
        SafeImageView(
            model = imageUrl,
            contentDescription = "",
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio((360 / 160).toFloat())
                    .blur(radius = blurRadius.value, BlurEdgeTreatment.UNBOUNDED)
                    .clip(RoundedCornerShape(20.dp)),
            onLoading = { ImageLoadingIndicator() },
            safetyLevel = SafetyLevel.OFF,
            onError = { ImageErrorIndicator() },
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun GuessPictureHintVisiblePreview() {
    AflamiTheme {
        GuessPicture(
            blurRadius = 8.dp,
            points = 10,
            imageUrl = "painterResource(R.drawable.bg_children_wearing_3d)",
            isHintVisible = true,
            onClick = {},
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun GuessPictureHintNotVisiblePreview() {
    AflamiTheme {
        GuessPicture(
            blurRadius = 8.dp,
            points = 10,
            imageUrl = "painterResource(R.drawable.bg_children_wearing_3d)",
            isHintVisible = false,
            onClick = {},
        )
    }
}
