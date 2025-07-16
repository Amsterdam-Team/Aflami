package com.example.ui.screens.search.sections.filterDialog.genre

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.viewmodel.common.TabOption
import com.example.viewmodel.search.searchByKeyword.SearchUiState
import com.example.viewmodel.search.mapper.getSelectedGenreType

@Composable
fun ScrollToGenreItem(
    lazyState: LazyListState,
    state: SearchUiState,
    isFilterCleared: Boolean,
    onFilterClearHandled: () -> Unit
) {
    LaunchedEffect(state.selectedTabOption, isFilterCleared) {
        when (state.selectedTabOption) {
            TabOption.MOVIES -> state.filterItemUiState.selectableMovieGenres
                .getSelectedGenreType().ordinal
                .also {
                    lazyState.smoothScroll(withAnimation = isFilterCleared, it)
                }

            TabOption.TV_SHOWS -> state.filterItemUiState.selectableTvShowGenres
                .getSelectedGenreType().ordinal
                .also {
                    lazyState.smoothScroll(withAnimation = isFilterCleared, it)
                }
        }
    }

    if (isFilterCleared) {
        onFilterClearHandled()
    }
}


private suspend fun LazyListState.smoothScroll(withAnimation: Boolean, targetIndex: Int) {
    return if (withAnimation) this.animateScrollToItem(targetIndex) else this.scrollToItem(targetIndex)
}