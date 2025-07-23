package com.example.viewmodel.home

import android.annotation.SuppressLint
import com.example.domain.useCase.GetHomeScreenDataUseCase
import com.example.entity.Movie
import com.example.viewmodel.shared.uiStates.MovieItemUiState
import java.text.DecimalFormat
import com.example.viewmodel.home.HomeUiState.PopularMovieItemUiState
import com.example.viewmodel.shared.uiStates.MovieItemUiState

class HomeUiStateMapper {
        @SuppressLint("DefaultLocale")
        fun toUiState(movies: List<Movie>): HomeUiState {
            return HomeUiState(
                popularMovies = movies.map { movie ->
                    HomeUiState.PopularMovieItemUiState(
                        name = movie.name,
                        rating = String.format("%.1f", movie.rating),
                        posterUrl = movie.posterUrl
                    )
                },
            )
        }

    fun toUpcomingMovieItemUiStates(movies: List<Movie>): List<MovieItemUiState> {
        return movies.map { movie ->
            MovieItemUiState(
                id = movie.id,
                name = movie.name,
                posterImageUrl = movie.posterUrl,
                yearOfRelease = movie.productionYear.toString(),
                rate = DecimalFormat("#.#").format(movie.rating).toString()
            )
        }
    }
    @SuppressLint("DefaultLocale")
    fun toUiState(homeScreenData: GetHomeScreenDataUseCase.HomeScreenData): HomeUiState {
        return HomeUiState(
            popularMovies = moviesToPopularMoviesUiState(homeScreenData.popularMovies),
            topRatedMovies = moviesToMoviesItemsUiState(homeScreenData.topRatedMovies),
            upcomingMovies =  moviesToMoviesItemsUiState(homeScreenData.upComingMovies)
        )
    }

    fun moviesToMoviesItemsUiState(movies: List<Movie>) = movies.map(::movieToMovieItemUiState)
    fun moviesToPopularMoviesUiState(movies: List<Movie>) = movies.map(::movieToPopularMovieUiState)

    @SuppressLint("DefaultLocale")
    fun movieToPopularMovieUiState(movie: Movie): PopularMovieItemUiState {
        return PopularMovieItemUiState(
            name = movie.name,
            rating = String.format("%.1f", movie.rating),
            posterUrl = movie.posterUrl
        )
    }

    @SuppressLint("DefaultLocale")
    fun movieToMovieItemUiState(movie: Movie): MovieItemUiState {
        return MovieItemUiState(
            id = movie.id,
            name = movie.name,
            rate = String.format("%.1f", movie.rating),
            posterImageUrl = movie.posterUrl,
            yearOfRelease = movie.productionYear.toString()
        )
    }

}