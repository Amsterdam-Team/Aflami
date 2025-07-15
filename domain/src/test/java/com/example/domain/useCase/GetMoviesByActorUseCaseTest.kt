package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
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
    fun `should call getMoviesByActor from movieRepository`() =
        runBlocking {
            getMoviesByActorUseCase("actorName")
            coVerify { movieRepository.getMoviesByActor(any()) }
        }

    @Test
    fun `should return movies sorted by popularity descending`() = runBlocking {
        coEvery { movieRepository.getMoviesByActor("actorName") } returns fakeMovieList

        val result = getMoviesByActorUseCase("actorName")
        assertThat(result).isEqualTo(fakeMovieList.sortedByDescending { it.popularity })
    }

    @Test
    fun `should return an empty list when repository returns empty`() = runBlocking {
        coEvery { movieRepository.getMoviesByActor(any()) } returns emptyList()

        val result = getMoviesByActorUseCase("nonexistentActor")
        assertThat(result).isEmpty()
    }

    private val fakeMovieList =
        listOf(
            Movie(
                id = 1,
                name = "Movie A",
                description = "",
                poster = "",
                productionYear = 2020,
                categories = emptyList(),
                rating = 7.0f,
                popularity = 50.0
            ),
            Movie(
                id = 2,
                name = "Movie B",
                description = "",
                poster = "",
                productionYear = 2021,
                categories = emptyList(),
                rating = 8.0f,
                popularity = 100.0
            ),
            Movie(
                id = 3,
                name = "Movie C",
                description = "",
                poster = "",
                productionYear = 2022,
                categories = emptyList(),
                rating = 6.0f,
                popularity = 20.0
            ),
        )
}
