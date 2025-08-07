package com.amsterdam.ui.screens.profile.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.designsystem.utils.ripple
import com.amsterdam.ui.R


@Composable
fun HistoryAndRatingSection(
    onClickHistory: () -> Unit,
    onClickRating: () -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CustomCard(
            imageResourceId = R.drawable.img_user_history,
            text = stringResource(R.string.watch_history),
            onClick = onClickHistory
        )
        CustomCard(
            imageResourceId = com.amsterdam.designsystem.R.drawable.img_user_rating,
            text = stringResource(R.string.my_rating),
            onClick = onClickRating
        )
    }
}


@Composable
private fun RowScope.CustomCard(
    @DrawableRes imageResourceId: Int,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.weight(1f)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(71.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = AppTheme.color.stroke,
                    shape = RoundedCornerShape(16.dp),
                )
                .background(
                    color = AppTheme.color.surfaceHigh,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = onClick,
                )
        ) {
            Text(
                text = text,
                style = AppTheme.textStyle.label.medium,
                color = AppTheme.color.title,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            )
        }
        Image(
            painter = painterResource(imageResourceId),
            contentDescription = stringResource(R.string.profile),
            modifier = Modifier
                .size(width = 64.dp, height = 71.dp)
                .align(Alignment.TopEnd)
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun HistoryAndRatingSectionPreview() {
    AflamiTheme {
        HistoryAndRatingSection(onClickHistory = {}, onClickRating = {})
    }
}