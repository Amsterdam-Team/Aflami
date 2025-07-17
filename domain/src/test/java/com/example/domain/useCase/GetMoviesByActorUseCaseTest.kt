package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.domain.useCase.utils.fakeMovieList
import com.example.entity.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMoviesByActorUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMoviesByActorUseCase: GetMoviesByActorUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMoviesByActorUseCase = GetMoviesByActorUseCase(movieRepository)
    }

    @Test
    fun `should call getMoviesByActor when an actor name is provided`() =
        runTest {
            getMoviesByActorUseCase("actorName")
            coVerify { movieRepository.getMoviesByActor(any()) }
        }

    @Test
    fun `should return movies sorted by popularity descending when data is available`() = runTest {
        coEvery { movieRepository.getMoviesByActor("actorName") } returns fakeMovieList

        val result = getMoviesByActorUseCase("actorName")
        assertThat(result).isEqualTo(fakeMovieList)
    }

    @Test
    fun `should return an empty list when repository returns no movies`() = runTest {
        coEvery { movieRepository.getMoviesByActor(any()) } returns emptyList()

        val result = getMoviesByActorUseCase("nonexistentActor")
        assertThat(result).isEmpty()
    }
}
