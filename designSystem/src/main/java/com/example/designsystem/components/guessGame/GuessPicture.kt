package com.example.designsystem.components.guessGame

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun GuessPicture(
    blurRadius: Dp,
    points: Int,
    painter: Painter,
    isHintVisible: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GuessCard(
        points = points,
        modifier = modifier,
        isHintVisible = isHintVisible,
        onClick = onClick
    ) {
        Image(
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio((360 / 160).toFloat())
                .blur(radius = blurRadius, BlurredEdgeTreatment.Unbounded)
                .clip(RoundedCornerShape(20.dp))
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
            painter = painterResource(R.drawable.bg_children_wearing_3d),
            isHintVisible = true,
            onClick = {}
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
            painter = painterResource(R.drawable.bg_children_wearing_3d),
            isHintVisible = false,
            onClick = {}
        )
    }
}
