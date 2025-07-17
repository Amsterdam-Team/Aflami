package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import com.example.domain.useCase.utils.fakeMovieList
import com.example.domain.useCase.utils.fakeMovieListWithCategories
import com.example.domain.useCase.utils.fakeMovieListWithRatings
import com.example.domain.useCase.utils.specificMovieList
import com.example.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMoviesByKeywordUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getAndFilterMoviesByKeywordUseCase: GetAndFilterMoviesByKeywordUseCase


    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getAndFilterMoviesByKeywordUseCase = GetAndFilterMoviesByKeywordUseCase(movieRepository)
    }

    @Test
    fun `should call getMoviesByKeyword when a keyword is provided`() = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieList

        getAndFilterMoviesByKeywordUseCase("keyword")
        coVerify(exactly = 1) { movieRepository.getMoviesByKeyword("keyword") }
    }

    @Test
    fun `should return empty list when filters yield an empty list`(): Unit =
        runTest {
            coEvery { movieRepository.getMoviesByKeyword(any()) } returns specificMovieList

            assertThat(
                getAndFilterMoviesByKeywordUseCase("keyword", rating = 10, MovieGenre.COMEDY)
            )
        }

    @Test
    fun `should return filtered movies when a minimum rating is specified`() = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithRatings

        val result = getAndFilterMoviesByKeywordUseCase("keyword", rating = 6)

        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(1)
        assertThat(result[1].id).isEqualTo(2)
    }

    @Test
    fun `should return all movies when rating filter is 0`() = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithRatings

        val result = getAndFilterMoviesByKeywordUseCase("keyword", rating = 0)

        assertThat(result).isEqualTo(fakeMovieListWithRatings)
    }

    @Test
    fun `should return filtered movies when a genre is specified`(): Unit = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithCategories

        val result = getAndFilterMoviesByKeywordUseCase("keyword", movieGenre = MovieGenre.ACTION)

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(
            fakeMovieListWithCategories[2],
            fakeMovieListWithCategories[0]
        )
    }

    @Test
    fun `should return all movies when genre filter is All`() = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithCategories

        val result = getAndFilterMoviesByKeywordUseCase("keyword", movieGenre = MovieGenre.ALL)

        assertThat(result).isEqualTo(fakeMovieListWithCategories)
    }

    @Test
    fun `should return empty list when no movies match the specified genre`(): Unit = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithCategories
        assertThat(getAndFilterMoviesByKeywordUseCase("keyword", movieGenre = MovieGenre.TV_MOVIE))
    }
}
