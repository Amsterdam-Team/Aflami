package com.amsterdam.ui.screens.home.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.amsterdam.ui.R
import com.amsterdam.designsystem.components.SectionTitle
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.components.MovieCard
import com.amsterdam.ui.screens.home.sections.placeholder.movieSectionPlaceholder
import com.amsterdam.ui.screens.search.actorSearch.MovieImage
import com.amsterdam.ui.utils.formateAsRate
import com.amsterdam.viewmodel.home.HomeUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType

fun LazyListScope.topRatingSection(
    state: HomeUiState.TopRatedMediaSectionUiState,
    onClickMediaItem: (Long, MediaType) -> Unit,
    onClickShowAll: () -> Unit,
    isVisible: Boolean
) {
    if (isVisible){
        if (state.isLoading){
            movieSectionPlaceholder()
        } else {
            item {
                SectionTitle(
                    title = stringResource(R.string.top_rating),
                    icon = painterResource(com.amsterdam.designsystem.R.drawable.ic_fire),
                    tintColor = AppTheme.color.secondary,
                    modifier = Modifier
                        .zIndex(1f)
                        .padding(top = 24.dp, bottom = 12.dp),
                    showAllLabel = true,
                    onAllLabelClicked = onClickShowAll
                )
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    items(state.mediaItems) { item ->
                        val movieType = if (item.mediaType == MediaType.MOVIE)
                            stringResource(R.string.movie)
                        else
                            stringResource(R.string.tv)

                        MovieCard(
                            movieImage = { MovieImage(item.posterImageUrl) },
                            movieType = movieType,
                            movieYear = item.yearOfRelease,
                            movieTitle = item.name,
                            movieRating = item.rate.formateAsRate()
                        ) {
                            onClickMediaItem(item.id, item.mediaType)
                        }
                    }
                }
            }
        }
    }
}