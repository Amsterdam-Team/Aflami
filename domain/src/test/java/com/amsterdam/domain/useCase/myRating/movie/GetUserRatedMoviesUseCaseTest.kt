package com.amsterdam.domain.useCase.myRating.movie


import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.entity.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetUserRatedMoviesUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetUserRatedMoviesUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        useCase = GetUserRatedMoviesUseCase(movieRepository)
    }

    @Test
    fun `getRatedMovies should return sorted list by user rate descending`() = runTest {
        // Given
        val movie1 = Movie(
            id = 1L,
            name = "Movie One",
            description = "",
            posterUrl = "",
            releaseDate = LocalDate.parse("2023-01-01"),
            categories = emptyList(),
            rating = 8.0f,
            popularity = 100.0,
            originCountry = "US",
            runTimeInMinutes = 120
        )
        val movie2 = movie1.copy(id = 2L, name = "Movie Two")

        val unsortedRatedMovies = listOf(
            UserRatedMovie(movie = movie1, userRate = 5),
            UserRatedMovie(movie = movie2, userRate = 9)
        )

        coEvery { movieRepository.getUserRatedMovies() } returns unsortedRatedMovies

        // When
        val result = useCase.getRatedMovies()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].userRate).isGreaterThan(result[1].userRate)
        assertThat(result[0].movie.name).isEqualTo("Movie Two")
    }
}
