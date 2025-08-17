package com.amsterdam.ui.screens.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.buttons.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.designsystem.utils.ripple
import com.amsterdam.ui.R

@Composable
fun OptionRow(
    title: String,
    modifier: Modifier = Modifier,
    leadingIcon: Painter,
    trailingContent: @Composable () -> Unit,
    onClick: () -> Unit={}
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick,
            )
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                paddingValues = PaddingValues(8.dp),
                painter = leadingIcon,
                withBorder = true,
                containerColor = AppTheme.color.surfaceHigh,
                tint = AppTheme.color.hint,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = title,
                color = AppTheme.color.body,
                style = AppTheme.textStyle.title.small
            )
        }
        trailingContent()
    }
}

@ThemeAndLocalePreviews
@Composable
private fun OptionRowPreview(){
    AflamiTheme {
        OptionRow(
            modifier = Modifier.padding(bottom = 12.dp),
            title = stringResource(R.string.content_restriction),
            leadingIcon = painterResource(R.drawable.ic_mood_saddizzy),
            trailingContent = {
                Icon(
                    painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            },
            onClick = {}
        )
    }
}