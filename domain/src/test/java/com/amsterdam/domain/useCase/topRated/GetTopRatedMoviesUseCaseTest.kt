package com.amsterdam.domain.useCase.topRated

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.utils.specificMovieList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTopRatedMoviesUseCaseTest {

    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private val getTopRatedMoviesUseCase by lazy {
        GetTopRatedMoviesUseCase(movieRepository)
    }


    @Test
    fun `should call getTopRatedMovies with default page when no page is provided`() = runTest {
        getTopRatedMoviesUseCase()

        coVerify(exactly = 1) { movieRepository.getTopRatedMovies(page = 1) }
    }

    @Test
    fun `should call getTopRatedMovies with specified page`() = runTest {
        val page = 5

        getTopRatedMoviesUseCase(page)

        coVerify(exactly = 1) { movieRepository.getTopRatedMovies(page = page) }
    }

    @Test
    fun `should return top rated movies when repository returns them`() = runTest {
        coEvery { movieRepository.getTopRatedMovies(any()) } returns specificMovieList

        val result = getTopRatedMoviesUseCase()

        assertThat(result).isEqualTo(specificMovieList)
    }

    @Test
    fun `should return empty list when repository returns an empty list`() = runTest {
        coEvery { movieRepository.getTopRatedMovies(any()) } returns emptyList()

        val result = getTopRatedMoviesUseCase()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository throws an exception`() = runTest {
        val expectedException = AflamiException()
        coEvery { movieRepository.getTopRatedMovies(any()) } throws expectedException

        val exception = assertThrows<AflamiException> {
            getTopRatedMoviesUseCase()
        }

        assertThat(exception).isEqualTo(expectedException)
    }
}