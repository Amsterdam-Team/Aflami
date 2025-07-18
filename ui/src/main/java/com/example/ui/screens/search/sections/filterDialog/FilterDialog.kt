package com.example.ui.screens.search.sections.filterDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.designsystem.R
import com.example.designsystem.components.Chip
import com.example.designsystem.components.Icon
import com.example.designsystem.components.IconButton
import com.example.designsystem.components.Text
import com.example.designsystem.components.buttons.ConfirmButton
import com.example.designsystem.components.buttons.OutlinedButton
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.ui.screens.search.sections.filterDialog.genre.ScrollToGenreItem
import com.example.ui.screens.search.sections.filterDialog.genre.getMovieGenreIcon
import com.example.ui.screens.search.sections.filterDialog.genre.getMovieGenreLabel
import com.example.ui.screens.search.sections.filterDialog.genre.getTvShowGenreIcon
import com.example.ui.screens.search.sections.filterDialog.genre.getTvShowGenreLabel
import com.example.viewmodel.search.mapper.getSelectedGenreType
import com.example.viewmodel.search.searchByKeyword.FilterItemUiState
import com.example.viewmodel.search.searchByKeyword.TabOption

@Composable
internal fun FilterDialog(
    filterState: FilterItemUiState,
    selectedTabOption: TabOption,
    onCancelButtonClicked: () -> Unit,
    onRatingStarChanged: (Int) -> Unit,
    onMovieGenreButtonChanged: (MovieGenre) -> Unit,
    onTvGenreButtonChanged: (TvShowGenre) -> Unit,
    onApplyButtonClicked: () -> Unit,
    onClearButtonClicked: () -> Unit,
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
        onFilterClearHandled = { isFilterCleared = false }
    )

    Dialog(
        onDismissRequest = { onCancelButtonClicked() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(0.9f)
                .background(
                    color = AppTheme.color.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.filter_result),
                    color = AppTheme.color.title,
                    fontStyle = AppTheme.textStyle.title.large.fontStyle,
                    style = AppTheme.textStyle.title.large,
                    modifier = Modifier
                        .weight(1f)
                )

                IconButton(
                    painter = painterResource(R.drawable.ic_cancel),
                    contentDescription = null,
                    onClick = { onCancelButtonClicked() },
                    tint = AppTheme.color.title
                )
            }
            Text(
                text = stringResource(R.string.imdb_rating),
                color = AppTheme.color.title,
                fontStyle = AppTheme.textStyle.title.small.fontStyle,
                style = AppTheme.textStyle.title.small,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            )
            RatingBar(
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp),
                selectedStarIndex = filterState.selectedStarIndex,
                onRatingStarChanged = onRatingStarChanged
            )
            Text(
                text = stringResource(R.string.genre),
                color = AppTheme.color.title,
                fontStyle = AppTheme.textStyle.title.small.fontStyle,
                style = AppTheme.textStyle.title.small,
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                state = lazyState,
                contentPadding = PaddingValues(horizontal = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically,
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
                                onClick = { onMovieGenreButtonChanged(genreType) },
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
                                onClick = { onTvGenreButtonChanged(category.selectableTvShowGenre.item) },
                            )
                        }
                    }
                }
            }
            ConfirmButton(
                title = stringResource(R.string.apply),
                onClick = onApplyButtonClicked,
                isEnabled = filterState.hasFilterData,
                isLoading = filterState.isLoading,
                isNegative = false,
                modifier = Modifier.padding(12.dp),
            )
            OutlinedButton(
                title = stringResource(R.string.clear),
                onClick = {
                    onClearButtonClicked()
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
private fun RatingBar(
    selectedStarIndex: Int,
    onRatingStarChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.padding(horizontal = 12.dp)
    ) {
        repeat(10) { index ->
            val starIndex = index + 1
            Icon(
                painter = painterResource(
                    id = if (selectedStarIndex >= starIndex)
                        R.drawable.ic_filled_star
                    else
                        R.drawable.ic_outlined_star
                ),
                contentDescription = null,
                tint = AppTheme.color.yellowAccent,
                modifier = Modifier
                    .size(24.dp)
                    .weight(1f)
                    .clickable(
                        onClick = { onRatingStarChanged(starIndex) },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
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
            onCancelButtonClicked = { },
            onRatingStarChanged = { },
            onMovieGenreButtonChanged = { },
            onTvGenreButtonChanged = { },
            onApplyButtonClicked = { },
            onClearButtonClicked = { },
        )
    }
}