package com.amsterdam.ui.screens.search.keywordSearch.sections

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.divider.HorizontalDivider
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.R

@Composable
internal fun RecentSearchesSection(
    modifier: Modifier = Modifier,
    recentSearches: List<String> = emptyList(),
    keyword: String = "",
    isLoading: Boolean = false,
    onAllRecentSearchesCleared: () -> Unit,
    onRecentSearchClicked: (String) -> Unit,
    onRecentSearchCleared: (String) -> Unit,
) {
    AnimatedContent(
        targetState = keyword,
        transitionSpec = {
            fadeIn(animationSpec = tween(1700)) togetherWith fadeOut(animationSpec = tween(1700))
        },
    ) { searchKeyword ->
        when {
            recentSearches.isNotEmpty() && searchKeyword.isBlank() -> {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(color = AppTheme.color.surface)
                        .padding(top = 24.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.recent_searches),
                            style = AppTheme.textStyle.title.medium,
                            color = AppTheme.color.title,
                            textAlign = TextAlign.Start,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                        )

                        Text(
                            modifier = Modifier.clickable(onClick = onAllRecentSearchesCleared),
                            text = stringResource(R.string.clear_all),
                            style = AppTheme.textStyle.label.medium,
                            color = AppTheme.color.primary,
                        )
                    }
                    Column(
                        modifier = Modifier
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

            searchKeyword.isBlank() && recentSearches.isEmpty() && !isLoading -> {
                val topPadding =
                    if (LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE) {
                        12.dp
                    } else {
                        72.dp
                    }
                ExploreMoviesAndShows(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = topPadding, bottom = 12.dp)
                )
            }

            isLoading -> {
                LoadingContainer(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = 48.dp)
                )
            }
        }
    }
}