package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.sp
import com.amsterdam.designsystem.components.ExpandableText
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.R
import com.amsterdam.ui.components.RatingChip
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ReviewUiState

@Composable
fun ReviewCard(
    review: ReviewUiState,
    modifier: Modifier = Modifier,
) {
    val strokeColor = AppTheme.color.stroke
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        color = strokeColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth,
                        cap = Stroke.DefaultCap,
                    )
                }
                .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            SafeImageView(
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = strokeColor,
                            shape = RoundedCornerShape(12.dp),
                        ),
                contentDescription = review.author,
                model =
                    review.imageUrl
                        ?: "android.resource://com.amsterdam.ui/${R.drawable.img_empty_user_pic}",
                contentScale = ContentScale.Crop,
                onLoading = { ImageLoadingIndicator() },
                onError = { ImageErrorIndicator() },
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
            ) {
                Text(
                    text = review.author,
                    maxLines = 1,
                    style =
                        AppTheme.textStyle.title.medium,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.color.title,
                    lineHeight = 28.sp,
                )
                Text(
                    text = review.username,
                    style = AppTheme.textStyle.label.small,
                    color = AppTheme.color.hint,
                    lineHeight = 16.sp,
                )
            }
            RatingChip(review.rating)
        }
        ExpandableText(
            text = review.content,
            textColor = AppTheme.color.body,
            modifier = Modifier.padding(top = 12.dp),
        )
        Text(
            text = review.date,
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.hint,
            modifier = Modifier.padding(top = 12.dp),
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ReviewCardPreview() {
    AflamiTheme {
        ReviewCard(
            review =
                ReviewUiState(
                    author = "Mohamed Ahmed",
                    username = "HamadaGanzabil",
                    rating = "5",
                    content = "While watching Disney’s new live-action version of Lilo & Stitch.",
                    date = "05-21-2014",
                    imageUrl = null,
                ),
        )
    }
}
