package com.amsterdam.ui.screens.letsPlay.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Text
import com.amsterdam.ui.utils.withEnglishDigits

@Composable
fun TotalScoreChip(totalScore: Int, modifier: Modifier = Modifier) {

    Row(
        modifier = modifier
            .background(
                brush = verticalGradient(AppTheme.color.pointsOverlayGradient),
                shape = RoundedCornerShape(100.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = pluralStringResource(com.amsterdam.ui.R.plurals.points_format, totalScore, totalScore).withEnglishDigits(),
            color = AppTheme.color.onPrimary,
            style = AppTheme.textStyle.label.small,
        )
        Icon(
            painter = painterResource(R.drawable.ic_user_pts),
            contentDescription = null,
            tint = AppTheme.color.onPrimary
        )
    }

}

@ThemeAndLocalePreviews
@Composable
private fun UpcomingCardPreview() {
    AflamiTheme {
        TotalScoreChip(totalScore = 275)
    }
}
