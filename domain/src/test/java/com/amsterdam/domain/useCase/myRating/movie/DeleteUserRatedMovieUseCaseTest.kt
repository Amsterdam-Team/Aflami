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

    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: DeleteUserRatedMovieUseCase

    @BeforeEach
    fun setUp() {
        movieRepository  = mockk(relaxed = true)
        useCase = DeleteUserRatedMovieUseCase(movieRepository)
    }

    @Test
    fun `deleteMovieRate should call deleteMovieRate on repository with correct movieId`() = runTest {
        // Given
        val movieId = 42L
        coEvery { movieRepository.deleteMovieRate(movieId) } just Runs
        // When
       val result = useCase.deleteMovieRate(movieId)

        // Then
        assertThat(result).isEqualTo(Unit)
        coVerify(exactly = 1) { movieRepository.deleteMovieRate(movieId) }
    }
}