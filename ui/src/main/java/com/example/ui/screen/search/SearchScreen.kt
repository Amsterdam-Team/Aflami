package com.example.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.viewmodel.common.TabOption
import com.example.viewmodel.search.GlobalSearchInteractionListener
import com.example.viewmodel.search.SearchUiState
import org.koin.androidx.compose.koinViewModel
import com.example.viewmodel.search.GlobalSearchViewModel

@Composable
fun SearchScreen(
    viewModel: GlobalSearchViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    SearchContent(state = state, interaction = viewModel)
}

@Composable
private fun SearchContent(state: SearchUiState, interaction: GlobalSearchInteractionListener, modifier: Modifier = Modifier) {
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
                onValueChange = interaction::onTextValuedChanged,
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
                        interaction.onTabOptionClicked(TabOption.entries[index])
                    },
                )
            }
        }

        item {
            if (state.query.isEmpty()){
                SuggestionsHubSection(
                    onWorldTourCardClick = interaction::onWorldSearchCardClicked,
                    onFindByActorCardClick = interaction::onActorSearchCardClicked
                )
            }
        }

        if (state.query.isEmpty()) {
            RecentSearchesSection(
                onClearAllClick = interaction::onClearAllRecentSearches,
                recentSearchItems = emptyList(),
                onRecentSearchItemClick = interaction::onRecentSearchClicked,
                onRecentSearchItemCancelClick = interaction::onClearRecentSearch
            )
        }
    }
}


