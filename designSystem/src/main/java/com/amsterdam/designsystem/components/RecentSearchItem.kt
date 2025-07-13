package com.amsterdam.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.theme.AppTheme

@Composable
fun RecentSearchItem(
    title: String,
    onCancelClick: (title: String) -> Unit,
    onItemClick: (title: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(title) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(R.drawable.ic_clock),
            tint = AppTheme.color.hint,
            contentDescription = title
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = title,
            style = AppTheme.textStyle.body.medium,
            color = AppTheme.color.title
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .clickable { onCancelClick(title) },
            painter = painterResource(R.drawable.ic_cancel),
            tint = AppTheme.color.hint,
            contentDescription = title
        )
    }
}