package com.example.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.TabsLayout
import com.example.designsystem.components.TextField
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AppTheme
import com.example.ui.screen.search.sections.RecentSearchesSection
import com.example.ui.screen.search.sections.SuggestionsHubSection
import com.example.viewmodel.search.RecentSearchItemUiState
import com.example.viewmodel.search.SearchUiState

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    SearchContent(state = SearchUiState())
}

@Composable
private fun SearchContent(state: SearchUiState, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = AppTheme.color.surface)
            .statusBarsPadding()
    ) {
        item { DefaultAppBar(modifier = Modifier.padding(horizontal = 16.dp), title = stringResource(R.string.search)) }

        stickyHeader {
            TextField(
                modifier = Modifier.background(color = AppTheme.color.surface).padding(top = 8.dp).padding(horizontal = 16.dp),
                text = "",
                hintText = stringResource(R.string.search_hint),
                trailingIcon = R.drawable.ic_filter_vertical,
                maxCharacters = 100
            )

            if (!state.isSearchQueryEmpty){
                TabsLayout(
                    modifier = Modifier.fillMaxWidth(),
                    tabs = listOf(stringResource(R.string.movies), stringResource(R.string.tv_shows), ),
                    selectedIndex = 0,
                    onSelectTab = { },
                )
            }
        }

        item {
            if (state.isSearchQueryEmpty){
                SuggestionsHubSection(
                    onWorldTourCardClick = {},
                    onFindByActorCardClick = {}
                )
            }
        }

        if (state.isSearchQueryEmpty) {
            RecentSearchesSection(
                onClearAllClick = {},
                recentSearchItems = dummyRecentSearchItems,
                onRecentSearchItemClick = {},
                onRecentSearchItemCancelClick = {}
            )
        }
    }
}


val dummyRecentSearchItems = mutableListOf<RecentSearchItemUiState>(
    RecentSearchItemUiState(
        title = "Recent 1"
    ),
    RecentSearchItemUiState(
        title = "Recent 2"
    ),
    RecentSearchItemUiState(
        title = "Recent 3"
    ),
)