package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Gender
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetMovieCastUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMovieCastUseCase: GetMovieCastUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMovieCastUseCase = GetMovieCastUseCase(movieRepository)
    }

    @Test
    fun `should call getActorsByMovieId and return actors sorted by popularity`() = runTest {
        val movieId = 1L
        val actor1 =
            Actor(id = 1, name = "Actor C", imageUrl = "", popularity = 50.0, gender = Gender.Male)
        val actor2 = Actor(
            id = 2,
            name = "Actor A",
            imageUrl = "",
            popularity = 100.0,
            gender = Gender.Female
        )
        val actor3 =
            Actor(id = 3, name = "Actor B", imageUrl = "", popularity = 75.0, gender = Gender.Male)

        coEvery { movieRepository.getActorsByMovieId(movieId) } returns listOf(
            actor1,
            actor2,
            actor3
        )

        val result = getMovieCastUseCase(movieId)

        coVerify(exactly = 1) { movieRepository.getActorsByMovieId(movieId) }
        assertThat(result).hasSize(3)
        assertThat(result).containsExactly(actor2, actor3, actor1)
            .inOrder()
    }

    @Test
    fun `should return an empty list when getActorsByMovieId returns empty`() = runTest {
        val movieId = 1L
        coEvery { movieRepository.getActorsByMovieId(movieId) } returns emptyList()

        val result = getMovieCastUseCase(movieId)

        coVerify(exactly = 1) { movieRepository.getActorsByMovieId(movieId) }
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when getActorsByMovieId fails`() = runTest {
        val movieId = 1L
        coEvery { movieRepository.getActorsByMovieId(movieId) } throws AflamiException()

        assertThrows<AflamiException> {
            getMovieCastUseCase(movieId)
        }
        coVerify(exactly = 1) { movieRepository.getActorsByMovieId(movieId) }
    }

    @Test
    fun `should handle a negative movie id and return an empty list`() = runTest {
        // Given
        val invalidMovieId = -1L
        coEvery { movieRepository.getActorsByMovieId(invalidMovieId) } returns emptyList()

        // When
        val result = getMovieCastUseCase(invalidMovieId)

        // Then
        coVerify(exactly = 1) { movieRepository.getActorsByMovieId(invalidMovieId) }
        assertThat(result).isEmpty()
    }
}