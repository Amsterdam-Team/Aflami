package com.amsterdam.ui.screens.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R

@Composable
fun ProfileInfoSection(username: String, userPoints: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "@$username",
            style = AppTheme.textStyle.label.medium,
            color = AppTheme.color.body,
            modifier = Modifier
                .padding(8.dp)
        )

        Row(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = AppTheme.color.pointsOverlayGradient
                    ),
                    shape = CircleShape
                )
                .padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$userPoints Pts.",
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.onPrimary,
                modifier = Modifier.padding(end = 4.dp)
            )
            Image(
                painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_user_pts),
                contentDescription = stringResource(R.string.profile),
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(AppTheme.color.onPrimary)
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ProfileInfoSectionPreview() {
    AflamiTheme {
        ProfileInfoSection("@Thoraya", 230)
    }
}