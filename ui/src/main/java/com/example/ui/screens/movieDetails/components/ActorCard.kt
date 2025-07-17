package com.example.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AppTheme
import com.example.imageviewer.ui.SafeImageView
import com.example.viewmodel.movieDetails.ActorUiState


@Composable
fun ActorCard(modifier: Modifier = Modifier, actor: ActorUiState) {
    Column(modifier = modifier.width(78.dp)) {
        SafeImageView(
            modifier = Modifier
                .size(78.dp)
                .clip(RoundedCornerShape(16.dp)),
            model = actor.photo,
            contentDescription = actor.name
        )
        Text(
            text = actor.name,
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.body,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
