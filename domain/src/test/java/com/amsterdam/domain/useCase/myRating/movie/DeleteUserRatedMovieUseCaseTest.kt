package com.amsterdam.domain.useCase.myRating.movie

import com.amsterdam.domain.repository.MovieRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteUserRatedMovieUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private val useCase by lazy {
        DeleteUserRatedMovieUseCase(movieRepository)
    }

    @Test
    fun `should call deleteMovieRate on repository with correct movieId`() =
        runTest {
            coEvery { movieRepository.deleteMovieRate(movieId) } just Runs

            val result = useCase.deleteMovieRate(movieId)

            assertThat(result).isEqualTo(Unit)
            coVerify(exactly = 1) { movieRepository.deleteMovieRate(movieId) }
        }

    private val movieId = 42L
}