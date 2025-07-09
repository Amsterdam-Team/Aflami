package com.example.ui.screen.search.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.RecentSearchItem
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AppTheme
import com.example.viewmodel.search.RecentSearchItemUiState

fun LazyListScope.RecentSearchesSection(
    onClearAllClick: () -> Unit,
    recentSearchItems: List<RecentSearchItemUiState>,
    onRecentSearchItemClick: (title: String) -> Unit,
    onRecentSearchItemCancelClick: (title: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!recentSearchItems.isEmpty()){
    stickyHeader {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(color = AppTheme.color.surface)
                .padding(top = 24.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.recent_searches),
                style = AppTheme.textStyle.title.medium,
                color = AppTheme.color.title,
                textAlign = TextAlign.Start
            )

            Text(
                modifier = modifier.clickable { onClearAllClick() },
                text = stringResource(R.string.clear_all),
                style = AppTheme.textStyle.label.medium,
                color = AppTheme.color.primary,
            )
        }
    }

        items(items = recentSearchItems, key = { it.title }) { recentSearchItem ->
            RecentSearchItem(
                modifier = Modifier.animateItem().padding(horizontal = 16.dp),
                title = recentSearchItem.title,
                onItemClick = { onRecentSearchItemClick(recentSearchItem.title) },
                onCancelClick = { onRecentSearchItemCancelClick(recentSearchItem.title) }
            )
        }
    } else {
        //todo: show empty state image
    }
}