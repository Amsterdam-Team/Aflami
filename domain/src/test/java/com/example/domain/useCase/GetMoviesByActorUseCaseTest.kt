package com.example.domain.useCase

import com.example.domain.exceptions.AflamiException
import com.example.domain.repository.MovieRepository
import com.example.domain.useCase.utils.fakeMovieList
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
    fun `getMoviesByActorUseCase should call getMoviesByActor exactly one time when called`() =
        runTest {
            getMoviesByActorUseCase("actorName")
            coVerify(exactly = 1) {
                movieRepository.getMoviesByActor(
                    any(),
                    page = 1,
                    moviesPerPage = 20
                )
            }
        }

    @Test
    fun `getMoviesByActorUseCase should return movies when data is available`() = runTest {
        coEvery {
            movieRepository.getMoviesByActor(
                "actorName",
                page = 1,
                moviesPerPage = 20
            )
        } returns fakeMovieList

        val result = getMoviesByActorUseCase("actorName")
        assertThat(result).isEqualTo(fakeMovieList)
    }

    @Test
    fun `getMoviesByActorUseCase should return an empty list when repository returns no movies`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByActor(
                    any(),
                    page = 1,
                    moviesPerPage = 20
                )
            } returns emptyList()

            val result = getMoviesByActorUseCase("nonexistentActor")
            assertThat(result).isEmpty()
        }

    @Test
    fun `getMoviesByActorUseCase should return Aflami exception when an error happened`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByActor(
                    "actorName",
                    page = 1,
                    moviesPerPage = 20
                )
            } throws AflamiException()
            assertThrows<AflamiException> { getMoviesByActorUseCase("actorName") }
        }
}
