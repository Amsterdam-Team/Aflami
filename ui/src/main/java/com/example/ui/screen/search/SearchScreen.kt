package com.example.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.TextField
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AppTheme
import com.example.ui.screen.search.sections.RecentSearchesSection
import com.example.ui.screen.search.sections.SuggestionsHubSection
import com.example.viewmodel.search.RecentSearchItemUiState

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    SearchContent()
}

@Composable
private fun SearchContent(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = AppTheme.color.surface)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        item { DefaultAppBar(title = stringResource(R.string.search)) }

        stickyHeader {
            TextField(
                modifier = Modifier.background(color = AppTheme.color.surface).padding(top = 8.dp),
                text = "",
                hintText = stringResource(R.string.search_hint),
                trailingIcon = R.drawable.ic_filter_vertical,
                maxCharacters = 100
            )
        }

        item {
            SuggestionsHubSection(
                onWorldTourCardClick = {},
                onFindByActorCardClick = {}
            )
        }

        RecentSearchesSection(
            onClearAllClick = {},
            recentSearchItems = emptyList(),
            onRecentSearchItemClick = {},
            onRecentSearchItemCancelClick = {}
        )
    }
}