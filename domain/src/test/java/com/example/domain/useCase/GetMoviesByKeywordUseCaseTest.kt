package com.example.domain.useCase

import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.repository.MovieRepository
import com.example.entity.Category
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


    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMoviesByKeywordUseCase = GetMoviesByKeywordUseCase(movieRepository)
    }

    @Test
    fun `should call getMoviesByKeyword when a keyword is provided`() = runBlocking {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieList

        getMoviesByKeywordUseCase("keyword")
        coVerify(exactly = 1) { movieRepository.getMoviesByKeyword("keyword") }
    }

    @Test
    fun `should return a list of movies sorted by popularity when keyword search is successful`() =
        runBlocking {
            coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieList

            val movies = getMoviesByKeywordUseCase("keyword")

            assertThat(fakeMovieList.sortedByDescending { it.popularity }).isEqualTo(movies)
        }

    @Test
    fun `should throw NoSearchByKeywordResultFoundException when repository returns an empty list`(): Unit =
        runBlocking {
            coEvery { movieRepository.getMoviesByKeyword(any()) } returns emptyList()

            assertThrows<NoSearchByKeywordResultFoundException> {
                getMoviesByKeywordUseCase("nonexistentKeyword")
            }
        }

    @Test
    fun `should throw NoSearchByKeywordResultFoundException when filters yield an empty list`(): Unit =
        runBlocking {
            val specificMovieList = listOf(
                Movie(
                    id = 1,
                    name = "Low Rated",
                    description = "",
                    poster = "",
                    productionYear = 2023,
                    categories = listOf(),
                    rating = 1.0f,
                    popularity = 5.0
                ),
                Movie(
                    id = 2,
                    name = "Wrong Category",
                    description = "",
                    poster = "",
                    productionYear = 2023,
                    categories = listOf(Category(id = 99, name = "Other", image = "")),
                    rating = 5.0f,
                    popularity = 5.0
                )
            )
            coEvery { movieRepository.getMoviesByKeyword(any()) } returns specificMovieList

            assertThrows<NoSearchByKeywordResultFoundException> {
                getMoviesByKeywordUseCase("keyword", rating = 10, movieGenreId = 1)
            }
        }

    @Test
    fun `should return filtered movies when a minimum rating is specified`() = runBlocking {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithRatings

        val result = getMoviesByKeywordUseCase("keyword", rating = 6)

        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(1)
        assertThat(result[1].id).isEqualTo(2)
    }

    @Test
    fun `should return all movies when rating filter is 0`() = runBlocking {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithRatings

        val result = getMoviesByKeywordUseCase("keyword", rating = 0)

        assertThat(result).isEqualTo(fakeMovieListWithRatings.sortedByDescending { it.popularity })
    }

    @Test
    fun `should return filtered movies when a genre ID is specified`(): Unit = runBlocking {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithCategories

        val result = getMoviesByKeywordUseCase("keyword", movieGenreId = 10)

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(
            fakeMovieListWithCategories[2],
            fakeMovieListWithCategories[0]
        )
    }

    @Test
    fun `should return all movies when genre ID filter is 0`() = runBlocking {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithCategories

        val result = getMoviesByKeywordUseCase("keyword", movieGenreId = 0)

        assertThat(result).isEqualTo(fakeMovieListWithCategories.sortedByDescending { it.popularity })
    }

    @Test
    fun `should throw NoSearchByKeywordResultFoundException when no movies match the specified genre`(): Unit = runBlocking {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithCategories

        assertThrows<NoSearchByKeywordResultFoundException> {
            getMoviesByKeywordUseCase("keyword", movieGenreId = 999)
        }
    }


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
    private val fakeMovieListWithRatings =
        listOf(
            Movie(
                id = 1,
                name = "High Rated",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 8.0f,
                popularity = 10.0
            ),
            Movie(
                id = 2,
                name = "Medium Rated",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 5.5f,
                popularity = 9.0
            ),  // Rounds to 6
            Movie(
                id = 3,
                name = "Low Rated",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 3.0f,
                popularity = 8.0
            ),
        )
    private val fakeMovieListWithCategories =
        listOf(
            Movie(
                id = 1,
                name = "Action Movie",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = listOf(Category(id = 10L, name = "Action", image = "")),
                rating = 8.0f,
                popularity = 10.0
            ),
            Movie(
                id = 2,
                name = "Comedy Movie",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = listOf(Category(id = 20L, name = "Comedy", image = "")),
                rating = 7.0f,
                popularity = 9.0
            ),
            Movie(
                id = 3,
                name = "Action & Drama",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = listOf(
                    Category(id = 10L, name = "Action", image = ""),
                    Category(id = 30L, name = "Drama", image = "")
                ),
                rating = 7.5f,
                popularity = 11.0
            ),
            Movie(
                id = 4,
                name = "Thriller",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = listOf(Category(id = 40L, name = "Thriller", image = "")),
                rating = 6.0f,
                popularity = 8.0
            )
        )
}
