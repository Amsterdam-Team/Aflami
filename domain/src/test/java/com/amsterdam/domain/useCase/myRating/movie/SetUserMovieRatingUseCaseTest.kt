package com.amsterdam.domain.useCase.myRating.movie

import com.amsterdam.domain.repository.MovieRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SetUserMovieRatingUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var setUserMovieRatingUseCase: SetUserMovieRatingUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        setUserMovieRatingUseCase = SetUserMovieRatingUseCase(movieRepository)
    }

    @Test
    fun `setUserMovieRate should call repository with correct parameters`() = runTest {
        // Given
        val rate = 8
        val movieId = 123L
        coEvery { movieRepository.setMovieRate(rate = rate, movieId = movieId) } returns Unit
        // When
       val result = setUserMovieRatingUseCase.setUserMovieRate(rate, movieId)

        // Then
        assertThat(result).isEqualTo(Unit)
        coVerify(exactly = 1) {
            movieRepository.setMovieRate(rate = rate, movieId = movieId)
        }
    }
}
