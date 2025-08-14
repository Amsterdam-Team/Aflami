package com.amsterdam.domain.useCase.myRating.movie

import com.amsterdam.domain.repository.MovieRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SetUserMovieRatingUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private val setUserMovieRatingUseCase by lazy {
        SetUserMovieRatingUseCase(movieRepository)
    }

    @Test
    fun `should call repository with correct parameters`() = runTest {
        coEvery { movieRepository.setMovieRate(rate = rate, movieId = movieId) } returns Unit

        val result = setUserMovieRatingUseCase.setUserMovieRate(rate, movieId)

        assertThat(result).isEqualTo(Unit)
        coVerify(exactly = 1) {
            movieRepository.setMovieRate(rate = rate, movieId = movieId)
        }
    }

    private val rate = 8
    private val movieId = 123L
}
