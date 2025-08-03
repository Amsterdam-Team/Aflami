package com.amsterdam.ui.screens.profile.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun SettingsSection() {
    CustomSettingCard(
        modifier = Modifier.padding(top = 24.dp),
        startIconResourceId = com.amsterdam.designsystem.R.drawable.ic_language,
        endIconResourceId = com.amsterdam.designsystem.R.drawable.ic_arrow_right,
        startText = stringResource(R.string.language),
        endText = "ENG",
    )

    CustomSettingCard(
        modifier = Modifier.padding(top = 8.dp),
        startIconResourceId = com.amsterdam.designsystem.R.drawable.ic_moon,
        endIconResourceId = com.amsterdam.designsystem.R.drawable.ic_arrow_right,
        startText = stringResource(R.string.app_theme),
        endText = "Dark",
    )

    CustomSettingCard(
        modifier = Modifier.padding(top = 8.dp),
        startIconResourceId = com.amsterdam.designsystem.R.drawable.ic_settings,
        endIconResourceId = com.amsterdam.designsystem.R.drawable.ic_arrow_right,
        startText = stringResource(R.string.settings)
    )
}


@Composable
private fun CustomSettingCard(
    @DrawableRes startIconResourceId: Int,
    @DrawableRes endIconResourceId: Int,
    startText: String,
    modifier: Modifier = Modifier,
    endText: String = ""
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .border(
                    width = 1.dp,
                    color = AppTheme.color.stroke,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = AppTheme.color.surfaceHigh,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(startIconResourceId),
                contentDescription = stringResource(R.string.profile),
                modifier = Modifier
                    .size(24.dp),
                colorFilter = ColorFilter.tint(AppTheme.color.body)
            )
        }

        Text(
            text = startText,
            style = AppTheme.textStyle.label.large,
            color = AppTheme.color.title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
        )

        if (endText.isNotBlank()) {
            Text(
                text = endText,
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.body,
                modifier = Modifier
                    .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
            )
        }

        Image(
            painter = painterResource(endIconResourceId),
            contentDescription = stringResource(R.string.profile),
            modifier = Modifier
                .size(20.dp),
            colorFilter = ColorFilter.tint(AppTheme.color.hint)
        )

    }
}

@ThemeAndLocalePreviews
@Composable
private fun SettingsSectionPreview() {
    AflamiTheme {
        SettingsSection()
    }
}