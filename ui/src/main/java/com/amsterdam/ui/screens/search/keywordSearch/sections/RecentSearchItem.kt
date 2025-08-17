package com.amsterdam.ui.screens.search.keywordSearch.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun RecentSearchItem(
    title: String,
    onCancelClick: (title: String) -> Unit,
    onItemClick: (title: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onItemClick(title) }
                .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(R.drawable.ic_clock),
            tint = AppTheme.color.hint,
            contentDescription = title,
        )

        Text(
            modifier = Modifier.padding(start = 8.dp).weight(1f),
            text = title,
            style = AppTheme.textStyle.body.medium,
            color = AppTheme.color.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Icon(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .clickable { onCancelClick(title) },
            painter = painterResource(R.drawable.ic_cancel),
            tint = AppTheme.color.hint,
            contentDescription = title,
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun RecentSearchItemPreview() {
    AflamiTheme {
        RecentSearchItem(
            title = "Recent Searchcccccccccccccccccccccccccccccccccccccccccccccccccc",
            onCancelClick = {},
            onItemClick = {},
        )
    }
}
