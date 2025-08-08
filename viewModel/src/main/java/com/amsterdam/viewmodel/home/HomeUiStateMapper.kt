package com.amsterdam.viewmodel.home

import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase.HomeScreenData
import com.amsterdam.entity.Movie
import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.viewmodel.home.HomeUiState.ContinueWatchingHomeItemUiState
import com.amsterdam.viewmodel.home.HomeUiState.MoodPickerItemUiState
import com.amsterdam.viewmodel.home.HomeUiState.PopularMediaItemUiState
import com.amsterdam.viewmodel.home.HomeUiState.PopularMediaSectionUiState
import com.amsterdam.viewmodel.home.HomeUiState.TopRatedMediaSectionUiState
import com.amsterdam.viewmodel.home.HomeUiState.TopRatedMoviesUiState
import com.amsterdam.viewmodel.home.HomeUiState.UpcomingMoviesUiState
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.utils.getMixedItemsList
import com.amsterdam.viewmodel.utils.toYearString

fun Movie.toMediaItemUiState(): TopRatedMoviesUiState {
    return TopRatedMoviesUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = releaseDate.toYearString(),
        rate = rating.toFormattedRating(),
        mediaType = MediaType.MOVIE
    )
}

fun TvShow.toMediaItemUiState(): TopRatedMoviesUiState {
    return TopRatedMoviesUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = airDate.toYearString(),
        rate = rating.toFormattedRating(),
        mediaType = MediaType.TV_SHOW
    )
}

fun Movie.toUpcomingMediaItemUiState(): UpcomingMoviesUiState {
    return UpcomingMoviesUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = releaseDate.toYearString(),
        rate = rating.toFormattedRating(),
        mediaType = MediaType.MOVIE
    )
}

fun Movie.toMoodPickerItemUiState(): MoodPickerItemUiState {
    return MoodPickerItemUiState(
        id = id,
        name = name,
        posterImageUrl = posterUrl,
        yearOfRelease = releaseDate.toYearString(),
        rate = rating.toFormattedRating(),
        mediaType = MediaType.MOVIE
    )
}

fun List<Movie>.toMoodPickerItemsUiState(): List<MoodPickerItemUiState> = map { it.toMoodPickerItemUiState() }

fun Movie.toPopularMediaItemUiState(): PopularMediaItemUiState {
    return PopularMediaItemUiState(
        id = id,
        name = name,
        rating = rating.toFormattedRating(),
        posterUrl = posterUrl,
        type = MediaType.MOVIE,
        categories = categories.map { it.name }
    )
}

fun TvShow.toPopularMediaItemUiState(): PopularMediaItemUiState {
    return PopularMediaItemUiState(
        id = id,
        name = name,
        rating = rating.toFormattedRating(),
        posterUrl = posterUrl,
        type = MediaType.TV_SHOW,
        categories = categories.map { it.name }
    )
}

fun MovieWatchHistory.toContinueWatchingMediaItemUiState(): ContinueWatchingHomeItemUiState{
    with(movie) {
        return ContinueWatchingHomeItemUiState(
            id = id,
            name = name,
            rate =  rating.toFormattedRating(),
            posterImageUrl = posterUrl,
            yearOfRelease = releaseDate.year.toString(),
            dateAdded = lastWatchedTime,
            mediaType = MediaType.MOVIE
        )
    }
}


fun TvShowWatchHistory.toContinueWatchingMediaItemUiState(): ContinueWatchingHomeItemUiState {
    with(tvShow) {
        return ContinueWatchingHomeItemUiState(
            id = id,
            name = name,
            rate =  rating.toFormattedRating(),
            posterImageUrl = posterUrl,
            yearOfRelease = airDate.year.toString(),
            dateAdded = lastWatchedTime,
            mediaType = MediaType.TV_SHOW
        )
    }
}

fun HomeScreenData.toHomeUiState(
    continueWatchingItems: List<ContinueWatchingHomeItemUiState>
): HomeUiState {
    return HomeUiState(
        popularMediaSectionUiState = PopularMediaSectionUiState(
            getMixedItemsList(
                popularMovies,
                popularTvShows,
                Movie::toPopularMediaItemUiState,
                TvShow::toPopularMediaItemUiState
            )
        ),
        topRatedMediaSectionUiState = TopRatedMediaSectionUiState(
            getMixedItemsList(
                topRatedMovies,
                topRatedTvShows,
                Movie::toMediaItemUiState,
                TvShow::toMediaItemUiState
            )
        ),
        upcomingMoviesSectionUiState = HomeUiState.UpcomingMoviesSectionUiState(
            movies = upComingMovies.map { it.toUpcomingMediaItemUiState() }
        ),
        continueWatchingMediaSectionUiState = HomeUiState.ContinueWatchingMediaSectionUiState(
            mediaItems = continueWatchingItems
        )
    )
}