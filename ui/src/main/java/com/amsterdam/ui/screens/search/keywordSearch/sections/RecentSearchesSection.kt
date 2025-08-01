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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.divider.HorizontalDivider
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.components.RecentSearchItem
import com.amsterdam.viewmodel.search.keywordSearch.SearchUiState

@Composable
internal fun RecentSearchesSection(
    state: SearchUiState,
    onAllRecentSearchesCleared: () -> Unit,
    onRecentSearchClicked: (String) -> Unit,
    onRecentSearchCleared: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = state,
        transitionSpec = {
            fadeIn(animationSpec = tween(1700)) togetherWith fadeOut(animationSpec = tween(1700))
        },
    ) {
        when {
            it.recentSearches.isNotEmpty() && it.keyword.isBlank() -> {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(color = AppTheme.color.surface)
                        .padding(top = 24.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.recent_searches),
                            style = AppTheme.textStyle.title.medium,
                            color = AppTheme.color.title,
                            textAlign = TextAlign.Start,
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
                        it.recentSearches.map { recentSearchItem ->
                            RecentSearchItem(
                                title = recentSearchItem,
                                onItemClick = onRecentSearchClicked,
                                onCancelClick = onRecentSearchCleared,
                            )
                            if (recentSearchItem != it.recentSearches.last()) HorizontalDivider()
                        }
                    }
                }
            }

            it.keyword.isEmpty() && it.recentSearches.isEmpty() && it.isLoading == false -> {
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

            it.isLoading -> {
                LoadingContainer(modifier = modifier
                    .fillMaxSize()
                    .padding(top = 48.dp))
            }
        }
    }
}