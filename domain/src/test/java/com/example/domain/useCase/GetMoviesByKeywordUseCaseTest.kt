package com.example.domain.useCase

import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.repository.MovieRepository
import com.example.domain.useCase.genreTypes.MovieGenre
import com.example.entity.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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

    @Test
    fun `should throw NoSearchByKeywordResultFoundException if repository returns empty list`(): Unit =
        runBlocking {
            coEvery { movieRepository.getMoviesByKeyword(any(), any(), any()) } returns emptyList()

            assertThrows<NoSearchByKeywordResultFoundException> {
                getMoviesByKeywordUseCase("nonexistent")
            }
        }

    @Test
    fun `should call getMoviesByKeyword with specified rating and genre`() = runBlocking {
        val testRating = 5.0f
        val testGenre = MovieGenre.ACTION

        coEvery {
            movieRepository.getMoviesByKeyword(
                any(),
                any(),
                any()
            )
        } returns fakeMovieList

        getMoviesByKeywordUseCase("action", rating = testRating, movieGenre = testGenre)

        coVerify(exactly = 1) {
            movieRepository.getMoviesByKeyword(
                "action",
                rating = testRating,
                movieGenre = testGenre
            )
        }
    }
}
