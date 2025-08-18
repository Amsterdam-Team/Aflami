package com.amsterdam.domain.useCase.upcoming

import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.amsterdam.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class GetUpcomingMoviesUseCaseTest {
    private val movieRepository: MovieRepository = mockk()
    private val getUpcomingMoviesUseCase by lazy {
        GetUpcomingMoviesUseCase(movieRepository)
    }


    @Test
    fun `should call getUpcomingMovies exactly once`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns emptyList()
        getUpcomingMoviesUseCase(MovieGenre.ALL)
        coVerify(exactly = 1) { movieRepository.getUpcomingMovies() }
    }

    @Test
    fun `should return all movies when genre is ALL`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns allMovies

        val result = getUpcomingMoviesUseCase(MovieGenre.ALL)

        assertThat(result).isEqualTo(allMovies)
    }

    @Test
    fun `should return movies that match a specific genre when genre is specified`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns allMovies

        val result = getUpcomingMoviesUseCase(MovieGenre.ACTION)

        assertThat(result).containsExactly(actionMovie, actionDramaMovie).inOrder()
    }

    @Test
    fun `should return an empty list when no movies match the genre`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns listOf(comedyMovie)

        val result = getUpcomingMoviesUseCase(MovieGenre.DRAMA)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return an empty list when repository returns no movies`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns emptyList()

        val result = getUpcomingMoviesUseCase(MovieGenre.ALL)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should exclude movies with empty category lists when filtering by genre`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns listOf(
            emptyCategoryMovie, dramaMovie
        )

        val result = getUpcomingMoviesUseCase(MovieGenre.DRAMA)

        assertThat(result).isEqualTo(listOf(dramaMovie))
    }

    @Test
    fun `should propagate exception when movie repository throws`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } throws NoInternetException()
        assertThrows<NoInternetException> { getUpcomingMoviesUseCase(MovieGenre.ALL) }
    }

    @Test
    fun `should return movies sorted by popularity then rating when multiple genres are present`() =
        runTest {
            coEvery { movieRepository.getUpcomingMovies() } returns listOf(
                highRatedButLessPopularMovie,
                popularAndWellRatedMovie,
                equallyPopularButLowerRatedMovie
            )

            val result = getUpcomingMoviesUseCase(MovieGenre.ACTION)

            assertThat(result).containsExactly(
                popularAndWellRatedMovie,
                equallyPopularButLowerRatedMovie,
                highRatedButLessPopularMovie
            ).inOrder()
        }

    private val baseMovie = fakeMovieList.first()

    private val actionMovie = baseMovie.copy(
        id = 1L, name = "Action Movie", categories = listOf(MovieGenre.ACTION)
    )

    private val dramaMovie = baseMovie.copy(
        id = 2L, name = "Drama Movie", categories = listOf(MovieGenre.DRAMA)
    )

    private val comedyMovie = baseMovie.copy(
        id = 3L, name = "Comedy Movie", categories = listOf(MovieGenre.COMEDY)
    )

    private val actionDramaMovie = baseMovie.copy(
        id = 4L,
        name = "Action Drama Movie",
        categories = listOf(MovieGenre.ACTION, MovieGenre.DRAMA)
    )

    private val emptyCategoryMovie = baseMovie.copy(
        id = 5L, name = "No Category Movie", categories = emptyList()
    )

    private val allMovies = listOf(actionMovie, dramaMovie, comedyMovie, actionDramaMovie)

    private val popularAndWellRatedMovie = baseMovie.copy(
        id = 1L,
        popularity = 150.0,
        categories = listOf(MovieGenre.ACTION),
        rating = 8.5f,
    )

    private val equallyPopularButLowerRatedMovie = baseMovie.copy(
        id = 2L, popularity = 150.0, categories = listOf(MovieGenre.ACTION), rating = 7.0f
    )

    private val highRatedButLessPopularMovie = baseMovie.copy(
        id = 3L, popularity = 100.0, categories = listOf(MovieGenre.ACTION), rating = 9.0f
    )
}