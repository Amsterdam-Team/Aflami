package com.example.domain.useCase

import com.example.domain.exceptions.AflamiException
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
    fun `getAndFilterMoviesByKeywordUseCase should call getMoviesByKeyword exactly one time`() = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieList
        getAndFilterMoviesByKeywordUseCase("keyword")
        coVerify(exactly = 1) { movieRepository.getMoviesByKeyword("keyword") }
    }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return empty list when filters yield an empty list`() =
        runTest {
            coEvery { movieRepository.getMoviesByKeyword(any()) } returns specificMovieList
            val result = getAndFilterMoviesByKeywordUseCase("keyword", rating = 10, MovieGenre.ANIMATION)
            assertThat(result).isEmpty()
        }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return filtered movies when a minimum rating is specified`() = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithRatings

        val result = getAndFilterMoviesByKeywordUseCase("keyword", rating = 6)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(1)
    }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return all movies when rating filter is 0`() = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithRatings
        val result = getAndFilterMoviesByKeywordUseCase("keyword", rating = 0)
        assertThat(result).isEqualTo(fakeMovieListWithRatings)
    }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return filtered movies when a genre is specified`() = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithCategories

        val result = getAndFilterMoviesByKeywordUseCase("keyword", movieGenre = MovieGenre.ACTION)

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(
            fakeMovieListWithCategories[0],
            fakeMovieListWithCategories[2]
        )
    }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return all movies when genre filter is All`() = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithCategories

        val result = getAndFilterMoviesByKeywordUseCase("keyword", movieGenre = MovieGenre.ALL)

        assertThat(result).isEqualTo(fakeMovieListWithCategories)
    }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return empty list when no movies match the specified genre`() = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns fakeMovieListWithCategories
        assertThat(getAndFilterMoviesByKeywordUseCase("keyword", movieGenre = MovieGenre.TV_MOVIE))
    }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return empty list when no movies returned`() = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } returns emptyList()
        assertThat(getAndFilterMoviesByKeywordUseCase("keyword"))
    }

    @Test
    fun `getAndFilterMoviesByKeywordUseCase should return Aflami exception when an error happened`() = runTest {
        coEvery { movieRepository.getMoviesByKeyword(any()) } throws AflamiException()
        assertThrows<AflamiException> { getAndFilterMoviesByKeywordUseCase("keyword") }
    }
}
