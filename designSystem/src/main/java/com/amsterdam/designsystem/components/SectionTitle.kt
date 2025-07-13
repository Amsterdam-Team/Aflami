package com.amsterdam.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews


@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    tintColor: Color = AppTheme.color.title,
    contentDescription: String? = null,
    showAllLabel: Boolean = false,
    onAllLabelClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = AppTheme.color.title,
                style = AppTheme.textStyle.headline.small
            )
            icon?.let {
                Icon(
                    painter = icon,
                    tint = tintColor,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        showAllLabel.takeIf { it }?.let {
            Text(
                text = stringResource(R.string.all),
                color = AppTheme.color.primary,
                style = AppTheme.textStyle.label.medium,
                modifier = Modifier
                    .clickable(
                        onClick = onAllLabelClicked,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )
        }
    }

}

@ThemeAndLocalePreviews
@Composable
private fun SectionTitlePreview() {
    AflamiTheme {
        SectionTitle(
            title = stringResource(R.string.movies_birthday),
            icon = painterResource(R.drawable.ic_birthday_cake),
            showAllLabel = true,
            tintColor = AppTheme.color.yellowAccent
        )
    }
}