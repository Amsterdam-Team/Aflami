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
    fun `invoke with ALL genre should return all movies`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns actionDramaMovies

        val result = getUpcomingMoviesUseCase(MovieGenre.ALL)

        assertThat(result).isEqualTo(actionDramaMovies)
    }

    @Test
    fun `invoke with a specific genre should return only movies matching that genre`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns actionDramaMovies

        val result = getUpcomingMoviesUseCase(MovieGenre.ACTION)

        assertThat(result).isEqualTo(listOf(actionMovie))
    }

    @Test
    fun `invoke with a specific genre and no matching movies should return an empty list`() =
        runTest {
            coEvery { movieRepository.getUpcomingMovies() } returns listOf(comedyMovie)

            val result = getUpcomingMoviesUseCase(MovieGenre.DRAMA)

            assertThat(result).isEmpty()
        }

    @Test
    fun `invoke should return an empty list when movie repository returns no movies`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns emptyList()

        val result = getUpcomingMoviesUseCase(MovieGenre.ALL)

        assertThat(result).isEmpty()
    }


    @Test
    fun `invoke should exclude movies with empty category lists when filtering by genre`() =
        runTest {
            coEvery { movieRepository.getUpcomingMovies() } returns listOf(
                emptyCategoryMovie,
                dramaMovie
            )

            val result = getUpcomingMoviesUseCase(MovieGenre.DRAMA)

            assertThat(result).isEqualTo(listOf(dramaMovie))
        }

    @Test
    fun `invoke should propagate exception when movie repository throws`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } throws NoInternetException()
        assertThrows<NoInternetException> { getUpcomingMoviesUseCase(MovieGenre.ALL) }
    }

    @Test
    fun `invoke should return movies sorted by popularity then rating`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns listOf(
            highRatedButLessPopularMovie,
            popularAndWellRatedMovie,
            equallyPopularButLowerRatedMovie
        )

        val result = getUpcomingMoviesUseCase(MovieGenre.ACTION)

        assertThat(result.map { it.name }).containsExactly(
            popularAndWellRatedMovie.name,
            equallyPopularButLowerRatedMovie.name,
            highRatedButLessPopularMovie.name
        ).inOrder()

        coVerify(exactly = 1) { movieRepository.getUpcomingMovies() }
    }

    private companion object {
        val baseMovie = Movie(
            id = 0L,
            name = "Base Movie",
            description = "A base movie for inheritance",
            posterUrl = "https://example.com/poster.jpg",
            productionYear = 2023u,
            categories = emptyList(),
            rating = 7.5f,
            popularity = 100.0,
            originCountry = "US",
            runTimeInMinutes = 120,
            hasVideo = true
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

        val emptyCategoryMovie = baseMovie.copy(
            id = 5L,
            name = "No Category Movie",
            categories = emptyList()
        )

        val actionDramaMovies = listOf(actionMovie, dramaMovie)

        val popularAndWellRatedMovie = Movie(
            id = 1L,
            name = "Popular Hero",
            description = "Blockbuster action movie",
            posterUrl = "url1",
            productionYear = 2024u,
            categories = listOf(MovieGenre.ACTION),
            rating = 8.5f,
            popularity = 150.0,
            originCountry = "US",
            runTimeInMinutes = 120,
            hasVideo = true
        )

        val equallyPopularButLowerRatedMovie = Movie(
            id = 2L,
            name = "Action Sequel",
            description = "Another action movie",
            posterUrl = "url2",
            productionYear = 2024u,
            categories = listOf(MovieGenre.ACTION),
            rating = 7.0f,
            popularity = 150.0,
            originCountry = "US",
            runTimeInMinutes = 110,
            hasVideo = false
        )

        val highRatedButLessPopularMovie = Movie(
            id = 3L,
            name = "Underrated Gem",
            description = "High quality but less known",
            posterUrl = "url3",
            productionYear = 2024u,
            categories = listOf(MovieGenre.ACTION),
            rating = 9.0f,
            popularity = 100.0,
            originCountry = "UK",
            runTimeInMinutes = 105,
            hasVideo = false
        )
    }
}