package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.models.Mood
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.home.GetMoviesByMoodUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMoviesByMoodUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var getMoviesByMoodUseCase: GetMoviesByMoodUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        getMoviesByMoodUseCase = GetMoviesByMoodUseCase(movieRepository)
    }

    @Test
    fun `should call movieRepository with correct genres for a given mood`() = runTest {
        // Given
        val mood = Mood.ROMANTIC
        val expectedGenres = listOf(MovieGenre.ROMANCE, MovieGenre.THRILLER)
        val expectedMovies = listOf(
            Movie(
                id = 1, name = "Romantic Movie", description = "", posterUrl = "",
                releaseDate = LocalDate(2023, 1, 1), categories = listOf(MovieGenre.COMEDY),
                rating = 1.0f, popularity = 1.0, originCountry = "", runTimeInMinutes = 1,
            )
        )
        coEvery { movieRepository.getMoviesByGenres(expectedGenres, any()) } returns expectedMovies

        // When
        val result = getMoviesByMoodUseCase(mood)

        // Then
        Truth.assertThat(result).isEqualTo(expectedMovies)
        coVerify(exactly = 1) { movieRepository.getMoviesByGenres(expectedGenres, any()) }
    }

//    @Test
//    fun `should return movies for the specified mood`() = runTest {
//        // Given
//        val mood = Mood.ANGRY
//        val expectedGenres = listOf(MovieGenre.HORROR, MovieGenre.THRILLER)
//        val moviesForAngryMood = listOf(
//            Movie(
//                id = 1, name = "Angry Movie", description = "", posterUrl = "",
//                releaseDate = LocalDate(2023, 1, 1), categories = listOf(MovieGenre.COMEDY),
//                rating = 1.0f, popularity = 1.0, originCountry = "", runTimeInMinutes = 1,
//            )
//        )
//        coEvery { movieRepository.getMoviesByGenres(expectedGenres, 1) } returns moviesForAngryMood
//
//        // When
//        val result = getMoviesByMoodUseCase(mood)
//
//        // Then
//        assertThat(result).isEqualTo(moviesForAngryMood)
//    }
//
//    @Test
//    fun `should return empty list when no movies match the mood`() = runTest {
//        // Given
//        val mood = Mood.ANGRY
//        val expectedGenres = listOf(MovieGenre.HORROR, MovieGenre.THRILLER)
//        coEvery { movieRepository.getMoviesByGenres(expectedGenres, 1) } returns emptyList<Movie>()
//
//        // When
//        val result = getMoviesByMoodUseCase(mood)
//
//        // Then
//        assertThat(result).isEqualTo(emptyList<Movie>())
//    }
//
//    @Test
//    fun `should propagate AflamiException when repository call fails`() = runTest {
//        // Given
//        val mood = Mood.ANGRY
//        val expectedGenres = listOf(MovieGenre.HORROR, MovieGenre.THRILLER)
//        coEvery { movieRepository.getMoviesByGenres(expectedGenres, 1) } throws AflamiException()
//
//        // When & Then
//        assertThrows<AflamiException> {
//            getMoviesByMoodUseCase(mood)
//        }
//    }
//}
}