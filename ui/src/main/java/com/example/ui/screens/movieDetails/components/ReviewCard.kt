package com.example.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.ExpandableText
import com.example.designsystem.components.RatingChip
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AppTheme
import com.example.imageviewer.ui.SafeImageView
import com.example.viewmodel.movieDetails.ReviewUiState



@Composable
fun ReviewCard(review: ReviewUiState, modifier: Modifier = Modifier) {
    val strokeColor = AppTheme.color.stroke
    Column(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = strokeColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth,
                    cap = Stroke.DefaultCap
                )
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            SafeImageView(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentDescription = review.author,
                model = review.imageUrl
                    ?: "android.resource://your.package.name/${R.drawable.ic_outlined_star}",
                contentScale = ContentScale.Crop,
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    modifier = Modifier.fillMaxWidth(), text =
                        review.author,
                    maxLines = 1,
                    style = AppTheme.textStyle.title.medium,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.color.title
                )
                Text(
                    text = review.username,
                    style = AppTheme.textStyle.label.small,
                    color = AppTheme.color.hint
                )

            }
            Spacer(Modifier.weight(1f))
            RatingChip(
                review.rating,
                modifier = Modifier
                    .size(40.dp)
                    .padding(bottom = 4.dp)
            )
        }
        ExpandableText(text = review.content)
        Text(
            text = review.date,
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.hint
        )
    }
}
