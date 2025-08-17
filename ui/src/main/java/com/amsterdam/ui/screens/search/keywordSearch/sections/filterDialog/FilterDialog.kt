package com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.amsterdam.ui.R
import com.amsterdam.designsystem.components.buttons.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.components.buttons.OutlinedButton
import com.amsterdam.designsystem.components.chip.Chip
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.ui.components.RatingBar
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.ScrollToGenreItem
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreIcon
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getMovieGenreLabel
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getTvShowGenreIcon
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.getTvShowGenreLabel
import com.amsterdam.viewmodel.search.keywordSearch.FilterInteractionListener
import com.amsterdam.viewmodel.search.keywordSearch.SearchUiState.FilterItemUiState
import com.amsterdam.viewmodel.search.mapper.getSelectedGenreType
import com.amsterdam.viewmodel.shared.TabOption

@Composable
internal fun FilterDialog(
    filterState: FilterItemUiState,
    selectedTabOption: TabOption,
    interaction: FilterInteractionListener,
    modifier: Modifier = Modifier,
) {
    val lazyState = rememberLazyListState()
    var isFilterCleared by remember { mutableStateOf(false) }

    ScrollToGenreItem(
        lazyState = lazyState,
        selectedTabOption = selectedTabOption,
        selectedMovieGenre = filterState.selectableMovieGenres.getSelectedGenreType(),
        selectedTvGenre = filterState.selectableTvShowGenres.getSelectedGenreType(),
        isFilterCleared = isFilterCleared,
        onFilterClearHandled = { isFilterCleared = false },
    )

    Dialog(
        onDismissRequest = interaction::onClickCancel,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
            ),
    ) {
        Column(
            modifier =
                modifier
                    .fillMaxWidth(0.9f)
                    .background(
                        color = AppTheme.color.surface,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 12.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp, start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.filter_result),
                    color = AppTheme.color.title,
                    style = AppTheme.textStyle.title.large,
                    modifier =
                        Modifier
                            .weight(1f),
                )

                IconButton(
                    painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_cancel),
                    contentDescription = "",
                    onClick = interaction::onClickCancel,
                    tint = AppTheme.color.title,
                )
            }
            Text(
                text = stringResource(R.string.imdb_rating),
                color = AppTheme.color.title,
                style = AppTheme.textStyle.title.small,
                modifier =
                    Modifier
                        .padding(horizontal = 12.dp),
            )
            RatingBar(
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp),
                selectedStarIndex = filterState.selectedStarIndex,
                onRatingStarChanged = interaction::onChangeRatingStar,
            )
            Text(
                text = stringResource(R.string.genre),
                color = AppTheme.color.title,
                style = AppTheme.textStyle.title.small,
                modifier =
                    Modifier
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
            )
            LazyRow(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                state = lazyState,
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                when (selectedTabOption) {
                    TabOption.MOVIES -> {
                        items(
                            items = filterState.selectableMovieGenres,
                            key = { it.selectableMovieGenre.item.name }
                        ) { category ->
                            val genreType = category.selectableMovieGenre.item
                            Chip(
                                icon = getMovieGenreIcon(genreType),
                                label = getMovieGenreLabel(genreType),
                                isSelected = category.selectableMovieGenre.isSelected,
                                onClick = { interaction.onChangeMovieGenre(genreType) },
                            )
                        }

                    }

                    TabOption.TV_SHOWS -> {
                        items(
                            items = filterState.selectableTvShowGenres,
                            key = { it.selectableTvShowGenre.item.name }
                        ) { category ->
                            val genreType = category.selectableTvShowGenre.item
                            Chip(
                                icon = getTvShowGenreIcon(genreType),
                                label = getTvShowGenreLabel(genreType),
                                isSelected = category.selectableTvShowGenre.isSelected,
                                onClick = { interaction.onChangeTvShowGenre(category.selectableTvShowGenre.item) },
                            )
                        }
                    }
                }
            }
            ConfirmButton(
                title = stringResource(R.string.apply),
                onClick = interaction::onClickApply,
                isEnabled = filterState.hasFilterData,
                isLoading = filterState.isLoading,
                isNegative = false,
                modifier = Modifier.padding(12.dp),
            )
            OutlinedButton(
                title = stringResource(R.string.clear),
                onClick = {
                    interaction.onClickClear()
                    isFilterCleared = true
                },
                isEnabled = true,
                isLoading = false,
                isNegative = false,
                modifier = Modifier.padding(horizontal = 12.dp),
            )
        }
    }
}

@Composable
@ThemeAndLocalePreviews
private fun FilterDialogPreview() {
    AflamiTheme {
        FilterDialog(
            filterState = FilterItemUiState(),
            selectedTabOption = TabOption.MOVIES,
            interaction = object : FilterInteractionListener {
                override fun onClickCancel() {}
                override fun onChangeRatingStar(ratingIndex: Int) {}
                override fun onChangeMovieGenre(genreType: MovieGenre) {}
                override fun onChangeTvShowGenre(genreType: TvShowGenre) {}
                override fun onClickApply() {}
                override fun onClickClear() {}

            }
        )
    }
}