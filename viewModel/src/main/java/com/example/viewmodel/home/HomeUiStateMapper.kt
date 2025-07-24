package com.example.viewmodel.home

import android.annotation.SuppressLint
import com.example.domain.useCase.GetHomeScreenDataUseCase
import com.example.entity.Movie
import com.example.viewmodel.shared.uiStates.MovieItemUiState
import com.example.viewmodel.home.HomeUiState.PopularMovieItemUiState

class HomeUiStateMapper {

    @SuppressLint("DefaultLocale")
    fun toUiState(homeScreenData: GetHomeScreenDataUseCase.HomeScreenData): HomeUiState {
        return HomeUiState(
            popularMovies = moviesToPopularMoviesUiState(homeScreenData.popularMovies),
            topRatedMovies = moviesToMoviesItemsUiState(homeScreenData.topRatedMovies),
            upcomingMovies =  moviesToMoviesItemsUiState(homeScreenData.upComingMovies)
        )
    }

    fun moviesToPopularMoviesUiState(movies: List<Movie>) = movies.map(::movieToPopularMovieUiState)
    fun moviesToMoviesItemsUiState(movies: List<Movie>) = movies.map(::movieToMovieItemUiState)

    @SuppressLint("DefaultLocale")
    private fun movieToPopularMovieUiState(movie: Movie): PopularMovieItemUiState {
        return PopularMovieItemUiState(
            name = movie.name,
            rating = String.format("%.1f", movie.rating),
            posterUrl = movie.posterUrl
        )
    }

    @SuppressLint("DefaultLocale")
    private fun movieToMovieItemUiState(movie: Movie): MovieItemUiState {
        return MovieItemUiState(
            id = movie.id,
            name = movie.name,
            rate = String.format("%.1f", movie.rating),
            posterImageUrl = movie.posterUrl,
            yearOfRelease = movie.releaseDate.year.toString()
        )
    }

}