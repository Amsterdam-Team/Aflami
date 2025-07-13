package com.example.ui.screens.search.sections

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.designsystem.components.buttons.PrimaryButton
import com.example.designsystem.components.buttons.SecondaryButton
import com.example.designsystem.theme.AppTheme
import com.example.ui.screens.search.sections.filterDialog.movieCategories
import com.example.ui.screens.search.sections.filterDialog.tvShowCategories
import com.example.viewmodel.common.TabOption
import com.example.viewmodel.search.FilterInteractionListener
import com.example.viewmodel.search.FilterItemUiState
import com.example.viewmodel.search.SearchUiState

@Composable
fun FilterDialog(
    state: SearchUiState,
    interaction: FilterInteractionListener,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = { interaction.onCancelButtonClicked() },
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
                    onClick = { interaction.onCancelButtonClicked() },
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
                state = state.filterItemUiState,
                interaction = interaction,
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
                contentPadding = PaddingValues(horizontal = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                when (state.selectedTabOption) {
                    TabOption.MOVIES -> {
                        items(state.filterItemUiState.movieGenreItemUiStates) { category ->
                            val genre = movieCategories[category.type]
                            Chip(
                                icon = painterResource(genre?.icon ?: R.drawable.ic_nav_categories),
                                label = stringResource(genre?.displayableName ?: R.string.all),
                                isSelected = category.isSelected,
                                onClick = { interaction.onMovieGenreButtonChanged(category.type) }
                            )
                        }
                    }

                    TabOption.TV_SHOWS -> {
                        items(state.filterItemUiState.tvShowGenreItemUiStates) { category ->
                            val genre = tvShowCategories[category.type]
                            Chip(
                                icon = painterResource(genre?.icon ?: R.drawable.ic_nav_categories),
                                label = stringResource(genre?.displayableName ?: R.string.all),
                                isSelected = category.isSelected,
                                onClick = { interaction.onTvGenreButtonChanged(category.type) }
                            )
                        }
                    }
                }
            }
            PrimaryButton(
                title = stringResource(R.string.apply),
                onClick = interaction::onApplyButtonClicked,
                isEnabled = state.filterItemUiState.hasFilterData,
                isLoading = state.filterItemUiState.isLoading,
                isNegative = false,
                modifier = Modifier.padding(12.dp),
            )
            SecondaryButton(
                title = stringResource(R.string.clear),
                onClick = interaction::onClearButtonClicked,
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
    state: FilterItemUiState,
    interaction: FilterInteractionListener,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.padding(horizontal = 12.dp)
    ) {
        repeat(10) { index ->
            val starIndex = index + 1
            Icon(
                painter = painterResource(
                    id = if (state.selectedStarIndex >= starIndex)
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
                        onClick = { interaction.onRatingStarChanged(starIndex) },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )
        }
    }
}

//@Composable
//@ThemeAndLocalePreviews
//fun FilterDialogPreview2() {
//    AflamiTheme {
//        FilterDialog(
//            state = FilterItemUiState(
//                selectedStarIndex = 5,
//            ),
//            interaction = object : FilterInteractionListener {
//                override fun onCancelButtonClicked() {}
//                override fun onRatingStarChanged(ratingIndex: Int) {}
//                override fun onMovieGenreButtonChanged(genreType: MovieCategoryType) {}
//                override fun onTvGenreButtonChanged(genreType: TvShowCategoryType) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onApplyButtonClicked() {}
//                override fun onClearButtonClicked() {}
//            },
//        )
//    }
//}