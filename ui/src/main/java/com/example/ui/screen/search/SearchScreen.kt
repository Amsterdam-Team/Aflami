package com.example.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.TabsLayout
import com.example.designsystem.components.TextField
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AppTheme
import com.example.ui.screen.search.sections.RecentSearchesSection
import com.example.ui.screen.search.sections.SuggestionsHubSection
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
                text = state.query,
                hintText = stringResource(R.string.search_hint),
                trailingIcon = R.drawable.ic_filter_vertical,
                isError = state.errorUiState != null,
                errorMessage = getErrorMessageBySearchErrorUiState(state.errorUiState),
                maxCharacters = 100
            )

            if (!state.query.isEmpty()){
                TabsLayout(
                    modifier = Modifier.fillMaxWidth(),
                    tabs = listOf(stringResource(R.string.movies), stringResource(R.string.tv_shows), ),
                    selectedIndex = state.selectedTabOption.index,
                    onSelectTab = { index ->

                    },
                )
            }
        }

        item {
            if (state.query.isEmpty()){
                SuggestionsHubSection(
                    onWorldTourCardClick = {},
                    onFindByActorCardClick = {}
                )
            }
        }

        if (state.query.isEmpty()) {
            RecentSearchesSection(
                onClearAllClick = {},
                recentSearchItems = emptyList(),
                onRecentSearchItemClick = {},
                onRecentSearchItemCancelClick = {}
            )
        }
    }
}

