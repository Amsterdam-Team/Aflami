package com.example.domain.useCase

import com.example.domain.exceptions.AflamiException
import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.example.entity.category.MovieGenre
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

    private val upcomingMoviesData = listOf(
        Movie(
            1,
            "Upcoming Action",
            "",
            "",
            (2025).toUInt(),
            listOf(MovieGenre.ACTION),
            7.0f,
            10.0,
            "USA",
            90,
            true
        ),
        Movie(
            2,
            "Upcoming Drama",
            "",
            "",
            (2025).toUInt(),
            listOf(MovieGenre.DRAMA),
            7.5f,
            12.0,
            "UK",
            110,
            true
        ),
        Movie(
            3,
            "Upcoming Comedy",
            "",
            "",
            (2025).toUInt(),
            listOf(MovieGenre.COMEDY),
            6.5f,
            8.0,
            "CAN",
            95,
            true
        ),
        Movie(
            4,
            "Upcoming Sci-Fi",
            "",
            "",
            (2025).toUInt(),
            listOf(MovieGenre.SCIENCE_FICTION),
            8.0f,
            15.0,
            "USA",
            130,
            true
        )
    )

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getUpcomingMoviesUseCase = GetUpcomingMoviesUseCase(movieRepository)
    }

    @Test
    fun `should call getUpcomingMovies exactly once`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns emptyList()
        getUpcomingMoviesUseCase(MovieGenre.ALL)
        coVerify(exactly = 1) { movieRepository.getUpcomingMovies() }
    }

    @Test
    fun `should return all upcoming movies when genre is ALL`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns upcomingMoviesData
        val result = getUpcomingMoviesUseCase(MovieGenre.ALL)
        assertThat(result).isEqualTo(upcomingMoviesData)
    }

    @Test
    fun `should return filtered upcoming movies when a specific genre is provided`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns upcomingMoviesData
        val result = getUpcomingMoviesUseCase(MovieGenre.ACTION)
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("Upcoming Action")
    }

    @Test
    fun `should return empty list when no movies match the specified genre`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns upcomingMoviesData
        val result = getUpcomingMoviesUseCase(MovieGenre.HORROR)
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when repository returns no upcoming movies`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } returns emptyList()
        val result = getUpcomingMoviesUseCase(MovieGenre.ALL)
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when getUpcomingMovies fails`() = runTest {
        coEvery { movieRepository.getUpcomingMovies() } throws AflamiException()
        assertThrows<AflamiException> { getUpcomingMoviesUseCase(MovieGenre.ALL) }
    }
}