package com.amsterdam.viewmodel.home.fake

import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase.ContinueWatchingScreenData
import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.utils.entityHelper.createMovie
import kotlinx.datetime.Clock

object FakeHomeScreenData {
    val upcomingMovies = listOf(
        createMovie(
            id = 101,
            name = "Galactic Odyssey",
            poster = "galactic_odyssey.jpg",
            rating = 9.1f,
        ),
        createMovie(
            id = 102,
            name = "Laugh Out Loud",
            poster = "laugh_out_loud.jpg",
            rating = 7.8f
        )
    )

    val expectedUiState = listOf(
        MovieItemUiState(
            id = 101
        ),
        MovieItemUiState(
            id = 102
        ),
    )

    val expectedMovies = listOf(
        createMovie(
            id = 101
        ),
        createMovie(
            id = 102
        ),
    )

    val continueMovieWatchHistory = MovieWatchHistory(
        createMovie(id = 103L, name = "continue movie"),
        Clock.System.now()
    )

    val continueWatchingData = ContinueWatchingScreenData(
        continueWatchingMovies = listOf(continueMovieWatchHistory),
        continueWatchingTvShows = emptyList()
    )

    val expectedComedyUiState = listOf(
        MovieItemUiState(
            id = 102L,
            name = "Laugh Out Loud",
            posterImageUrl = "laugh_out_loud.jpg",
            yearOfRelease = "2025",
            rate = "7.8"
        )
    )

    val comedyGenre = MovieGenre.COMEDY
    val upcomingComedyMovies = listOf(
        createMovie(
            id = 102,
            name = "Laugh Out Loud",
            poster = "laugh_out_loud.jpg",
            rating = 7.8f
        )
    )
}