package com.amsterdam.viewmodel.home

import android.annotation.SuppressLint
import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.home.HomeUiState.PopularMediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.utils.getMixedItemsList

class HomeUiStateMapper {
    @SuppressLint("DefaultLocale")
    fun toUiState(
        homeScreenData: GetHomeScreenDataUseCase.HomeScreenData,
        continueWatchingItems: List<MediaItemUiState>
    ): HomeUiState {
        return HomeUiState(
            popularMediaItems = getPopularMediaItems(
                homeScreenData.popularMovies,
                homeScreenData.popularTvShows
            ),
            topRatedMediaItems = getTopRatedMediaItems(
                homeScreenData.topRatedMovies,
                homeScreenData.topRatedTvShows
            ),
            upcomingMovies = moviesToMoviesItemsUiState(homeScreenData.upComingMovies),
            continueWatchingItems = continueWatchingItems
        )
    }

    private fun getPopularMediaItems(
        popularMovies: List<Movie>,
        popularTvShows: List<TvShow>
    ): List<PopularMediaItemUiState> {
        return getMixedItemsList(
            popularMovies,
            popularTvShows,
            ::movieToPopularMediaItemUiState,
            ::tvShowToPopularMediaItemUiState
        )
    }

    private fun getTopRatedMediaItems(
        topRatedMovies: List<Movie>,
        topRatedTvShows: List<TvShow>
    ): List<MediaItemUiState> {
        return getMixedItemsList(
            topRatedMovies,
            topRatedTvShows,
            ::movieToMediaItemUiState,
            ::tvShowToMediaItemUiState
        )
    }

    fun moviesToMoviesItemsUiState(movies: List<Movie>) = movies.map(::movieToMovieItemUiState)

    @SuppressLint("DefaultLocale")
    private fun movieToPopularMediaItemUiState(movie: Movie): PopularMediaItemUiState {
        return PopularMediaItemUiState(
            id = movie.id,
            name = movie.name,
            rating = String.format("%.1f", movie.rating),
            posterUrl = movie.posterUrl,
            type = MediaType.MOVIE
        )
    }

    @SuppressLint("DefaultLocale")
    private fun tvShowToPopularMediaItemUiState(tvShow: TvShow): PopularMediaItemUiState {
        return PopularMediaItemUiState(
            id = tvShow.id,
            name = tvShow.name,
            rating = String.format("%.1f", tvShow.rating),
            posterUrl = tvShow.posterUrl,
            type = MediaType.TV_SHOW
        )
    }

    @SuppressLint("DefaultLocale")
    fun movieToMediaItemUiState(movie: Movie): MediaItemUiState {
        return MediaItemUiState(
            id = movie.id,
            name = movie.name,
            rate = String.format("%.1f", movie.rating),
            posterImageUrl = movie.posterUrl,
            yearOfRelease = movie.releaseDate.year.toString(),
            mediaType = MediaType.MOVIE
        )
    }

    @SuppressLint("DefaultLocale")
    fun tvShowToMediaItemUiState(tvShow: TvShow): MediaItemUiState {
        return MediaItemUiState(
            id = tvShow.id,
            name = tvShow.name,
            rate = String.format("%.1f", tvShow.rating),
            posterImageUrl = tvShow.posterUrl,
            yearOfRelease = tvShow.airDate.year.toString(),
            mediaType = MediaType.TV_SHOW
        )
    }

    @SuppressLint("DefaultLocale")
    fun movieToMovieItemUiState(movie: Movie): MovieItemUiState {
        return MovieItemUiState(
            id = movie.id,
            name = movie.name,
            rate = String.format("%.1f", movie.rating),
            posterImageUrl = movie.posterUrl,
            yearOfRelease = movie.releaseDate.year.toString()
        )
    }
}