package com.example.ui.screens.search.sections.filterDialog.genre

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.viewmodel.search.searchByKeyword.TabOption

@Composable
internal fun ScrollToGenreItem(
    lazyState: LazyListState,
    selectedTabOption: TabOption,
    selectedMovieGenre: MovieGenre,
    selectedTvGenre: TvShowGenre,
    isFilterCleared: Boolean,
    onFilterClearHandled: () -> Unit
) {
    LaunchedEffect(selectedTabOption, isFilterCleared) {
        when (selectedTabOption) {
            TabOption.MOVIES -> selectedMovieGenre.ordinal
                .also {
                    lazyState.smoothScroll(withAnimation = isFilterCleared, it)
                }

            TabOption.TV_SHOWS -> selectedTvGenre.ordinal
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
    return if (withAnimation) this.animateScrollToItem(targetIndex) else this.scrollToItem(
        targetIndex
    )
}