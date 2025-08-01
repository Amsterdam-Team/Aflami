package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.search.GetMoviesByActorUseCase
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetMoviesByActorUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMoviesByActorUseCase: GetMoviesByActorUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMoviesByActorUseCase = GetMoviesByActorUseCase(movieRepository)
    }

    @Test
    fun `should call getMoviesByActor with correct default parameters`() = runTest {
        // Given
        val actorName = "actorName"

        // When
        getMoviesByActorUseCase(actorName)

        // Then
        coVerify(exactly = 1) {
            movieRepository.getMoviesByActor(
                actorName = actorName,
                page = 1,
                moviesPerPage = 20
            )
        }
    }

    @Test
    fun `should call getMoviesByActor with correct non-default parameters`() = runTest {
        // Given
        val actorName = "actorName"
        val page = 2
        val moviesPerPage = 10

        // When
        getMoviesByActorUseCase(actorName, page, moviesPerPage)

        // Then
        coVerify(exactly = 1) {
            movieRepository.getMoviesByActor(
                actorName = actorName,
                page = page,
                moviesPerPage = moviesPerPage
            )
        }
    }

    @Test
    fun `should return movies when repository returns data`() = runTest {
        // Given
        val actorName = "actorName"
        coEvery {
            movieRepository.getMoviesByActor(
                actorName = actorName,
                page = any(),
                moviesPerPage = any()
            )
        } returns fakeMovieList

        // When
        val result = getMoviesByActorUseCase(actorName)

        // Then
        assertThat(result).isEqualTo(fakeMovieList)
    }

    @Test
    fun `should return an empty list when repository returns no movies`() = runTest {
        // Given
        val actorName = "nonexistentActor"
        coEvery {
            movieRepository.getMoviesByActor(
                actorName = actorName,
                page = any(),
                moviesPerPage = any()
            )
        } returns emptyList()

        // When
        val result = getMoviesByActorUseCase(actorName)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository call fails`() = runTest {
        // Given
        val actorName = "actorName"
        coEvery {
            movieRepository.getMoviesByActor(
                actorName = actorName,
                page = any(),
                moviesPerPage = any()
            )
        } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> { getMoviesByActorUseCase(actorName) }
        coVerify(exactly = 1) {
            movieRepository.getMoviesByActor(
                actorName = actorName,
                page = 1,
                moviesPerPage = 20
            )
        }
    }
}