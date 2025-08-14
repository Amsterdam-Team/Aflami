package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.models.Mood
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetMoviesByMoodUseCaseTest {

    private val movieRepository: MovieRepository = mockk()
    private val getMoviesByMoodUseCase by lazy {
        GetMoviesByMoodUseCase(movieRepository)
    }

    @Test
    fun `should call movieRepository with correct genres for a given mood`() = runTest {
        val expectedGenres = sadMood.movieGenres
        coEvery { movieRepository.getMoviesByGenres(expectedGenres, any()) } returns emptyList()

        getMoviesByMoodUseCase(sadMood)

        coVerify(exactly = 1) { movieRepository.getMoviesByGenres(expectedGenres, any()) }
    }

    @Test
    fun `should return movies for the specified mood`() = runTest {
        val expectedGenres = angryMood.movieGenres
        val moviesForAngryMood = listOf(fakeMovieList.first().copy(categories = expectedGenres))
        coEvery {
            movieRepository.getMoviesByGenres(
                expectedGenres,
                any()
            )
        } returns moviesForAngryMood

        val result = getMoviesByMoodUseCase(angryMood)

        assertThat(result).isEqualTo(moviesForAngryMood)
    }

    @Test
    fun `should return empty list when no movies match the mood`() = runTest {
        val expectedGenres = depressedMood.movieGenres
        coEvery { movieRepository.getMoviesByGenres(expectedGenres, any()) } returns emptyList()

        val result = getMoviesByMoodUseCase(depressedMood)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should propagate AflamiException when repository call fails`() = runTest {
        val expectedGenres = angryMood.movieGenres
        coEvery {
            movieRepository.getMoviesByGenres(
                expectedGenres,
                any()
            )
        } throws AflamiException()

        assertThrows<AflamiException> {
            getMoviesByMoodUseCase(angryMood)
        }
    }

    private val sadMood = Mood.SAD
    private val angryMood = Mood.ANGRY
    private val depressedMood = Mood.DEPRESSED
}