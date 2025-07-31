package com.amsterdam.ui.screens.search.keywordSearch.sections

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.divider.HorizontalDivider
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.components.RecentSearchItem

@Composable
internal fun RecentSearchesSection(
    keyword: String,
    recentSearches: List<String>,
    onAllRecentSearchesCleared: () -> Unit,
    onRecentSearchClicked: (String) -> Unit,
    onRecentSearchCleared: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(recentSearches.isNotEmpty() && keyword.isBlank()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = AppTheme.color.surface)
                .padding(top = 24.dp),
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.recent_searches),
                    style = AppTheme.textStyle.title.medium,
                    color = AppTheme.color.title,
                    textAlign = TextAlign.Start,
                )

                Text(
                    modifier = modifier.clickable(onClick = onAllRecentSearchesCleared),
                    text = stringResource(R.string.clear_all),
                    style = AppTheme.textStyle.label.medium,
                    color = AppTheme.color.primary,
                )
            }
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                recentSearches.map { recentSearchItem ->
                    RecentSearchItem(
                        title = recentSearchItem,
                        onItemClick = onRecentSearchClicked,
                        onCancelClick = onRecentSearchCleared,
                    )
                    if (recentSearchItem != recentSearches.last()) HorizontalDivider()
                }
            }
        }
    }

    AnimatedVisibility(keyword.isEmpty() && recentSearches.isEmpty()) {
        val topPadding = if (LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE) {
            12.dp
        } else {
            72.dp
        }
        ExploreMoviesAndShows(Modifier.fillMaxWidth().padding(top = topPadding, bottom = 12.dp))
    }
}
