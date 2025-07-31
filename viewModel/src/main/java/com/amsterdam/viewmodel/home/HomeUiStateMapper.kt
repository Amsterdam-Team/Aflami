package com.amsterdam.viewmodel.home

import android.annotation.SuppressLint
import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase.HomeScreenData
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.home.HomeUiState.ContinueWatchingMediaItemUiState
import com.amsterdam.viewmodel.home.HomeUiState.PopularMediaItemUiState
import com.amsterdam.viewmodel.home.HomeUiState.PopularMediaSectionUiState
import com.amsterdam.viewmodel.home.HomeUiState.TopRatedMediaSectionUiState
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import com.amsterdam.viewmodel.utils.getMixedItemsList
import javax.inject.Inject

class HomeUiStateMapper @Inject constructor(){
    @SuppressLint("DefaultLocale")
    fun toUiState(
        homeScreenData: HomeScreenData,
        continueWatchingItems: List<ContinueWatchingMediaItemUiState>
    ): HomeUiState {
        return HomeUiState(
            popularMediaSectionUiState = getPopularMediaItems(
                homeScreenData.popularMovies,
                homeScreenData.popularTvShows
            ),
            topRatedMediaSectionUiState = getTopRatedMediaItems(
                homeScreenData.topRatedMovies,
                homeScreenData.topRatedTvShows
            ),
            upcomingMoviesSectionUiState = HomeUiState.UpcomingMoviesSectionUiState(
                movies = moviesToMoviesItemsUiState(homeScreenData.upComingMovies)
            ),
            continueWatchingMediaSectionUiState = HomeUiState.ContinueWatchingMediaSectionUiState(
                mediaItems = continueWatchingItems
            )
        )
    }

    private fun getPopularMediaItems(
        popularMovies: List<Movie>,
        popularTvShows: List<TvShow>
    ): PopularMediaSectionUiState {
        return PopularMediaSectionUiState(
            getMixedItemsList(
                popularMovies,
                popularTvShows,
                ::movieToPopularMediaItemUiState,
                ::tvShowToPopularMediaItemUiState
            )
        )
    }

    private fun getTopRatedMediaItems(
        topRatedMovies: List<Movie>,
        topRatedTvShows: List<TvShow>
    ): TopRatedMediaSectionUiState {
        return TopRatedMediaSectionUiState(
            getMixedItemsList(
                topRatedMovies,
                topRatedTvShows,
                ::movieToMediaItemUiState,
                ::tvShowToMediaItemUiState
            )
        )
    }

    @SuppressLint("DefaultLocale")
    private fun movieToPopularMediaItemUiState(movie: Movie): PopularMediaItemUiState {
        return PopularMediaItemUiState(
            id = movie.id,
            name = movie.name,
            rating = if (movie.rating % 1 == 0.0f) "${movie.rating.toInt()}" else "%.1f".format(movie.rating),
            posterUrl = movie.posterUrl,
            type = MediaType.MOVIE,
            category = movie.categories.map { it.name }
        )
    }

    @SuppressLint("DefaultLocale")
    private fun tvShowToPopularMediaItemUiState(tvShow: TvShow): PopularMediaItemUiState {
        return PopularMediaItemUiState(
            id = tvShow.id,
            name = tvShow.name,
            rating = if (tvShow.rating % 1 == 0.0f) "${tvShow.rating.toInt()}" else "%.1f".format(tvShow.rating),
            posterUrl = tvShow.posterUrl,
            type = MediaType.TV_SHOW,
            category = tvShow.categories.map { it.name }
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

    fun moviesToMoviesItemsUiState(movies: List<Movie>) = movies.map(::movieToMovieItemUiState)
}