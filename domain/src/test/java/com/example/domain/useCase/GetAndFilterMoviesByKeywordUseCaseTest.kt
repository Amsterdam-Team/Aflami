package com.example.domain.useCase

import com.example.domain.exceptions.AflamiException
import com.example.domain.repository.MovieRepository
import com.example.domain.useCase.utils.fakeMovieList
import com.example.domain.useCase.utils.fakeMovieListWithCategories
import com.example.domain.useCase.utils.fakeMovieListWithRatings
import com.example.domain.useCase.utils.specificMovieList
import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetAndFilterMoviesByKeywordUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getAndFilterMoviesByKeywordUseCase: GetAndFilterMoviesByKeywordUseCase


    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getAndFilterMoviesByKeywordUseCase = GetAndFilterMoviesByKeywordUseCase(movieRepository)
    }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should call getMoviesByKeyword exactly one time`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByKeyword(
                    any(),
                    page = 1,
                    moviesPerPage = 20
                )
            } returns fakeMovieList
            getAndFilterMoviesByKeywordUseCase("keyword")
            coVerify(exactly = 1) {
                movieRepository.getMoviesByKeyword(
                    "keyword",
                    page = 1,
                    moviesPerPage = 20
                )
            }
        }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return empty list when filters yield an empty list`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByKeyword(
                    any(),
                    page = 1,
                    moviesPerPage = 20
                )
            } returns specificMovieList
            val result = getAndFilterMoviesByKeywordUseCase(
                "keyword",
                rating = 10,
                page = 1,
                movieGenre = MovieGenre.ANIMATION
            )
            assertThat(result).isEmpty()
        }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return filtered movies when a minimum rating is specified`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByKeyword(
                    any(),
                    page = 1,
                    moviesPerPage = 20
                )
            } returns fakeMovieListWithRatings

            val result = getAndFilterMoviesByKeywordUseCase("keyword", rating = 6)

            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo(1)
        }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return all movies when rating filter is 0`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByKeyword(
                    any(),
                    page = 1,
                    moviesPerPage = 20
                )
            } returns fakeMovieListWithRatings
            val result = getAndFilterMoviesByKeywordUseCase("keyword", rating = 0)
            assertThat(result).isEqualTo(fakeMovieListWithRatings)
        }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return filtered movies when a genre is specified`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByKeyword(
                    any(),
                    page = 1,
                    moviesPerPage = 20
                )
            } returns fakeMovieListWithCategories

            val result =
                getAndFilterMoviesByKeywordUseCase("keyword", movieGenre = MovieGenre.ACTION)

            assertThat(result).hasSize(2)
            assertThat(result).containsExactly(
                fakeMovieListWithCategories[0],
                fakeMovieListWithCategories[2]
            )
        }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return all movies when genre filter is All`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByKeyword(
                    any(),
                    page = 1,
                    moviesPerPage = 20
                )
            } returns fakeMovieListWithCategories

            val result = getAndFilterMoviesByKeywordUseCase("keyword", movieGenre = MovieGenre.ALL)

            assertThat(result).isEqualTo(fakeMovieListWithCategories)
        }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return empty list when no movies match the specified genre`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByKeyword(
                    any(),
                    page = 1,
                    moviesPerPage = 20
                )
            } returns fakeMovieListWithCategories
            assertThat(
                getAndFilterMoviesByKeywordUseCase(
                    "keyword",
                    movieGenre = MovieGenre.TV_MOVIE
                )
            )
        }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return empty list when no movies returned`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByKeyword(
                    any(),
                    page = 1,
                    moviesPerPage = 20
                )
            } returns emptyList()
            assertThat(getAndFilterMoviesByKeywordUseCase("keyword"))
        }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return Aflami exception when an error happened`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByKeyword(
                    any(),
                    page = 1,
                    moviesPerPage = 20
                )
            } throws AflamiException()
            assertThrows<AflamiException> { getAndFilterMoviesByKeywordUseCase("keyword") }
        }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should throw AflamiException when getAllGenreInterests fails`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByKeyword(
                    any(),
                    any(),
                    any()
                )
            } returns fakeMovieList
            coEvery { movieRepository.getAllGenreInterests() } throws AflamiException()

            assertThrows<AflamiException> { getAndFilterMoviesByKeywordUseCase("keyword") }
        }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should sort movies by user interest in categories`() =
        runTest {
            val movie1 = Movie(
                id = 1,
                name = "Sci-Fi Action",
                description = "",
                posterUrl = "",
                productionYear = (2023).toUInt(),
                categories = listOf(MovieGenre.SCIENCE_FICTION, MovieGenre.ACTION),
                rating = 7.0f,
                popularity = 10.0,
                originCountry = "",
                runTime = 1,
                hasVideo = true
            )
            val movie2 = Movie(
                id = 2,
                name = "Drama Comedy",
                description = "",
                posterUrl = "",
                productionYear = (2023).toUInt(),
                categories = listOf(MovieGenre.DRAMA, MovieGenre.COMEDY),
                rating = 8.0f,
                popularity = 12.0,
                originCountry = "",
                runTime = 1,
                hasVideo = true
            )
            val movie3 = Movie(
                id = 3,
                name = "Family Adventure",
                description = "",
                posterUrl = "",
                productionYear = (2023).toUInt(),
                categories = listOf(MovieGenre.FAMILY, MovieGenre.ADVENTURE),
                rating = 7.5f,
                popularity = 9.0,
                originCountry = "",
                runTime = 1,
                hasVideo = true
            )

            val moviesFromRepo = listOf(movie1, movie2, movie3)

            coEvery { movieRepository.getAllGenreInterests() } returns mapOf(
                MovieGenre.ACTION to 100,
                MovieGenre.COMEDY to 50,
                MovieGenre.DRAMA to 80,
                MovieGenre.SCIENCE_FICTION to 70,
                MovieGenre.FAMILY to 20,
                MovieGenre.ADVENTURE to 30
            )

            coEvery {
                movieRepository.getMoviesByKeyword(
                    any(),
                    any(),
                    any()
                )
            } returns moviesFromRepo

            val result = getAndFilterMoviesByKeywordUseCase("keyword")

            assertThat(result).containsExactly(movie1, movie2, movie3).inOrder()
        }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should handle movies with empty categories in sorting`() =
        runTest {
            val movieWithEmptyCategories = Movie(
                id = 4,
                name = "No Category",
                description = "",
                posterUrl = "",
                productionYear = (2023).toUInt(),
                categories = emptyList(),
                rating = 6.0f,
                popularity = 5.0,
                originCountry = "",
                runTime = 1,
                hasVideo = true
            )
            val movieWithCategories = Movie(
                id = 5,
                name = "Has Category",
                description = "",
                posterUrl = "",
                productionYear = (2023).toUInt(),
                categories = listOf(MovieGenre.DRAMA),
                rating = 7.0f,
                popularity = 8.0,
                originCountry = "",
                runTime = 1,
                hasVideo = true
            )

            coEvery { movieRepository.getAllGenreInterests() } returns mapOf(MovieGenre.DRAMA to 50)
            coEvery { movieRepository.getMoviesByKeyword(any(), any(), any()) } returns listOf(
                movieWithEmptyCategories,
                movieWithCategories
            )

            val result = getAndFilterMoviesByKeywordUseCase("keyword")

            assertThat(result).containsExactly(movieWithCategories, movieWithEmptyCategories)
                .inOrder()
        }


    @Test
    fun `getAndFilterMoviesByKeywordUseCase should filter by both rating and genre`() = runTest {
        coEvery {
            movieRepository.getMoviesByKeyword(
                any(),
                any(),
                any()
            )
        } returns fakeMovieListWithCategories

        val result = getAndFilterMoviesByKeywordUseCase(
            "keyword",
            rating = 7,
            movieGenre = MovieGenre.ACTION
        )

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(
            fakeMovieListWithCategories[0],
            fakeMovieListWithCategories[2]
        ).inOrder()
    }
}
