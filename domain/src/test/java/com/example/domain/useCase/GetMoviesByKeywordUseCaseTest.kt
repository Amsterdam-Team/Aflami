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

class GetMoviesByKeywordUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMoviesByKeywordUseCase: GetMoviesByKeywordUseCase
    private val fakeMovieList =
        listOf(
            Movie(
                id = 1,
                name = "abc",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 2.5f,
                popularity = 10.2,
            ),
            Movie(
                id = 2,
                name = "dfg",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 2.5f,
                popularity = 11.2,
            ),
            Movie(
                id = 3,
                name = "hij",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 2.5f,
                popularity = 0.2,
            ),
        )

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMoviesByKeywordUseCase = GetMoviesByKeywordUseCase(movieRepository)
    }

    @Test
    fun `should call getMoviesByKeyword from movieRepository`() = runBlocking {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieList

            getMoviesByKeywordUseCase("keyword")
            coVerify(exactly = 1) { movieRepository.getMoviesByKeyword("keyword") }
        }

    @Test
    fun `should return a list of movies sorted by popularity`() =
        runBlocking {
            coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieList

            val movies = getMoviesByKeywordUseCase("keyword")

            assertThat(fakeMovieList.sortedByDescending { it.popularity }).isEqualTo(movies)
        }
}
