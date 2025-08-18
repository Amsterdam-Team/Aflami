package com.amsterdam.viewmodel.listDetails

import com.amsterdam.domain.useCase.list.GetListMediaItemsFromListUseCase.ListDetailsMediaItems
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.listDetails.ListDetailsUiState.ListDetailsItemsUiState
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.utils.getLinearItemsList
import com.amsterdam.viewmodel.utils.toYearString

fun Movie.toListDetailsItemUiState(): ListDetailsItemsUiState {
    return ListDetailsItemsUiState(
        id = id,
        name = name,
        yearOfRelease = releaseDate.toYearString(),
        posterImageUrl = posterUrl,
        rate = rating.toFormattedRating(),
        mediaType = MediaType.MOVIE,
        isAdult = isAdult
    )
}

fun TvShow.toListDetailsItemUiState(): ListDetailsItemsUiState {
    return ListDetailsItemsUiState(
        id = id,
        name = name,
        yearOfRelease = airDate.toYearString(),
        posterImageUrl = posterUrl,
        rate = rating.toFormattedRating(),
        mediaType = MediaType.TV_SHOW
    )
}

fun ListDetailsMediaItems.toListDetailsItemUiState(): List<ListDetailsItemsUiState> {
    return getLinearItemsList(
        this.listDetailsMovies,
        this.listDetailsShows,
        Movie::toListDetailsItemUiState,
        TvShow::toListDetailsItemUiState
    )
}
