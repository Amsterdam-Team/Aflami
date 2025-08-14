package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetMoviesByActorUseCaseTest {
    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private val getMoviesByActorUseCase by lazy {
        GetMoviesByActorUseCase(movieRepository)
    }

    @Test
    fun `should call getMoviesByActor with correct default parameters`() = runTest {
        getMoviesByActorUseCase(actorName)

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
        getMoviesByActorUseCase(actorName, page, moviesPerPage)

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
        coEvery {
            movieRepository.getMoviesByActor(
                actorName = actorName,
                page = any(),
                moviesPerPage = any()
            )
        } returns fakeMovieList

        val result = getMoviesByActorUseCase(actorName)

        assertThat(result).isEqualTo(fakeMovieList)
    }

    @Test
    fun `should return an empty list when repository returns no movies`() = runTest {
        coEvery {
            movieRepository.getMoviesByActor(
                actorName = actorName,
                page = any(),
                moviesPerPage = any()
            )
        } returns emptyList()

        val result = getMoviesByActorUseCase(actorName)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository call fails`() = runTest {
        coEvery {
            movieRepository.getMoviesByActor(
                actorName = actorName,
                page = any(),
                moviesPerPage = any()
            )
        } throws AflamiException()

        assertThrows<AflamiException> { getMoviesByActorUseCase(actorName) }
        coVerify(exactly = 1) {
            movieRepository.getMoviesByActor(
                actorName = actorName,
                page = 1,
                moviesPerPage = 20
            )
        }
    }

    private val actorName = "actorName"
    private val page = 2
    private val moviesPerPage = 10
}