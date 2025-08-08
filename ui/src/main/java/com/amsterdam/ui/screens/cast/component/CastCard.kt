package com.amsterdam.ui.screens.cast.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.application.LocalRestrictionLevel
import com.amsterdam.ui.utils.toSafetyLevel
import com.amsterdam.viewmodel.cast.CastUiState.ActorUiState


@Composable
fun CastCard(modifier: Modifier = Modifier, actor: ActorUiState) {
    val safetyLevel = LocalRestrictionLevel.current.toSafetyLevel()
    Column(modifier = modifier.width(78.dp)) {
        SafeImageView(
            modifier = Modifier
                .size(78.dp)
                .clip(RoundedCornerShape(16.dp)),
            model = actor.actorImage,
            contentDescription = actor.actorName,
            safetyLevel = safetyLevel,
            onLoading = { ImageLoadingIndicator() },
            onError = { ImageErrorIndicator() },
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = actor.actorName,
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.body,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ActorCardPreview() {
    AflamiTheme {
        CastCard(
            actor = ActorUiState(
                actorName = "Emma Watson",
                actorImage = "https://example.com/emma_watson.jpg"
            )
        )
    }
}