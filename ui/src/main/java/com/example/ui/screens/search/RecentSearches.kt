package com.example.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.RecentSearchItem
import com.example.designsystem.components.Text
import com.example.designsystem.components.divider.HorizontalDivider
import com.example.designsystem.theme.AppTheme
import com.example.viewmodel.search.GlobalSearchInteractionListener
import com.example.viewmodel.search.SearchUiState

@Composable
fun RecentSearchesSection(
    state: SearchUiState,
    interaction: GlobalSearchInteractionListener,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(state.recentSearches.isNotEmpty()) {
        Column {
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
                    modifier = modifier.clickable(onClick = interaction::onClearAllRecentSearches),
                    text = stringResource(R.string.clear_all),
                    style = AppTheme.textStyle.label.medium,
                    color = AppTheme.color.primary,
                )
            }

            LazyColumn {
                items(items = state.recentSearches, key = { it }) { recentSearchItem ->
                    RecentSearchItem(
                        modifier = Modifier.animateItem(),
                        title = recentSearchItem,
                        onItemClick = interaction::onRecentSearchClicked,
                        onCancelClick = { interaction::onClearRecentSearch }
                    )
                    if (recentSearchItem != state.recentSearches.last()) HorizontalDivider()
                }
            }
        }

    }


    AnimatedVisibility(state.recentSearches.isEmpty()) {

        //todo: show empty state image

    }

}