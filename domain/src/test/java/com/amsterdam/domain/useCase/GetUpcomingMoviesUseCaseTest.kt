package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.home.GetUpcomingMoviesUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class GetUpcomingMoviesUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        getUpcomingMoviesUseCase = GetUpcomingMoviesUseCase(movieRepository)
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
    fun `should return movies that match a specific genre`() = runTest {
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
    fun `should exclude movies with empty category lists when filtering by genre`() =
        runTest {
            coEvery { movieRepository.getUpcomingMovies() } returns listOf(
                emptyCategoryMovie,
                dramaMovie
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

    private companion object {
        val baseMovie = Movie(
            id = 0L,
            name = "Base Movie",
            description = "A base movie for inheritance",
            posterUrl = "https://example.com/poster.jpg",
            releaseDate = LocalDate(2023, 1, 1),
            categories = emptyList(),
            rating = 7.5f,
            popularity = 100.0,
            originCountry = "US",
            runTimeInMinutes = 120,
            hasVideo = true,
            productionCompanies = emptyList()
        )

        val actionMovie = baseMovie.copy(
            id = 1L,
            name = "Action Movie",
            categories = listOf(MovieGenre.ACTION)
        )

        val dramaMovie = baseMovie.copy(
            id = 2L,
            name = "Drama Movie",
            categories = listOf(MovieGenre.DRAMA)
        )

        val comedyMovie = baseMovie.copy(
            id = 3L,
            name = "Comedy Movie",
            categories = listOf(MovieGenre.COMEDY)
        )

        val actionDramaMovie = baseMovie.copy(
            id = 4L,
            name = "Action Drama Movie",
            categories = listOf(MovieGenre.ACTION, MovieGenre.DRAMA)
        )

        val emptyCategoryMovie = baseMovie.copy(
            id = 5L,
            name = "No Category Movie",
            categories = emptyList()
        )

        val allMovies = listOf(actionMovie, dramaMovie, comedyMovie, actionDramaMovie)

        val popularAndWellRatedMovie = Movie(
            id = 1L,
            name = "Popular Hero",
            description = "Blockbuster action movie",
            posterUrl = "url1",
            releaseDate = LocalDate(2024, 1, 1),
            categories = listOf(MovieGenre.ACTION),
            rating = 8.5f,
            popularity = 150.0,
            originCountry = "US",
            runTimeInMinutes = 120,
            hasVideo = true,
            productionCompanies = emptyList()
        )

        val equallyPopularButLowerRatedMovie = Movie(
            id = 2L,
            name = "Action Sequel",
            description = "Another action movie",
            posterUrl = "url2",
            releaseDate = LocalDate(2024, 1, 1),
            categories = listOf(MovieGenre.ACTION),
            rating = 7.0f,
            popularity = 150.0,
            originCountry = "US",
            runTimeInMinutes = 110,
            hasVideo = false,
            productionCompanies = emptyList()
        )

        val highRatedButLessPopularMovie = Movie(
            id = 3L,
            name = "Underrated Gem",
            description = "High quality but less known",
            posterUrl = "url3",
            releaseDate = LocalDate(2024, 1, 1),
            categories = listOf(MovieGenre.ACTION),
            rating = 9.0f,
            popularity = 100.0,
            originCountry = "UK",
            runTimeInMinutes = 105,
            hasVideo = false,
            productionCompanies = emptyList()
        )
    }
}