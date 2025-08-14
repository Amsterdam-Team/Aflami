package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.amsterdam.domain.useCase.utils.fakeMovieListWithCategories
import com.amsterdam.domain.useCase.utils.fakeMovieListWithRatings
import com.amsterdam.domain.useCase.utils.specificMovieList
import com.amsterdam.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetAndFilterMoviesByKeywordUseCaseTest {
    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private val getAndFilterMoviesByKeywordUseCase by lazy {
        GetAndFilterMoviesByKeywordUseCase(movieRepository)
    }


    @Test
    fun `should call getMoviesByKeyword exactly one time`() =
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
    fun `should return empty list when filters yield an empty list`() =
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
    fun `should return filtered movies when a minimum rating is specified`() =
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
    fun `should return all movies when rating filter is 0`() =
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
    fun `should return filtered movies when a genre is specified`() =
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
    fun `should return all movies when genre filter is All`() =
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
    fun `should return empty list when no movies match the specified genre`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByKeyword(
                    any(),
                    page = 1,
                    moviesPerPage = 20
                )
            } returns fakeMovieListWithCategories
            val result =
                getAndFilterMoviesByKeywordUseCase("keyword", movieGenre = MovieGenre.TV_MOVIE)
            assertThat(result).isEmpty()
        }

    @Test
    fun `should return empty list when no movies returned`() =
        runTest {
            coEvery {
                movieRepository.getMoviesByKeyword(
                    any(),
                    page = 1,
                    moviesPerPage = 20
                )
            } returns emptyList()
            val result = getAndFilterMoviesByKeywordUseCase("keyword")
            assertThat(result).isEmpty()
        }

    @Test
    fun `should throw AflamiException when an error happens`() =
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
    fun `should filter by both rating and genre`() = runTest {
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

    @Test
    fun `should pass correct pagination parameters`() =
        runTest {
            val keyword = "test"
            val page = 2
            val moviesPerPage = 10

            coEvery {
                movieRepository.getMoviesByKeyword(
                    keyword = keyword,
                    page = page,
                    moviesPerPage = moviesPerPage
                )
            } returns fakeMovieList

            getAndFilterMoviesByKeywordUseCase(keyword, page, moviesPerPage)

            coVerify(exactly = 1) {
                movieRepository.getMoviesByKeyword(
                    keyword = keyword,
                    page = page,
                    moviesPerPage = moviesPerPage
                )
            }
        }
}