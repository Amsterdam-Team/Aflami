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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetMovieCastUseCaseTest {
    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private val getMovieCastUseCase by lazy {
        GetMovieCastUseCase(movieRepository)
    }

    @Test
    fun `should call getActorsByMovieId and return actors sorted by popularity`() = runTest {
        coEvery { movieRepository.getActorsByMovieId(movieId) } returns listOf(
            actor1, actor2, actor3
        )

        val result = getMovieCastUseCase(movieId)

        coVerify(exactly = 1) { movieRepository.getActorsByMovieId(movieId) }
        assertThat(result).hasSize(3)
        assertThat(result).containsExactly(actor2, actor3, actor1).inOrder()
    }

    @Test
    fun `should return an empty list when getActorsByMovieId returns empty`() = runTest {
        coEvery { movieRepository.getActorsByMovieId(movieId) } returns emptyList()

        val result = getMovieCastUseCase(movieId)

        coVerify(exactly = 1) { movieRepository.getActorsByMovieId(movieId) }
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when getActorsByMovieId fails`() = runTest {
        coEvery { movieRepository.getActorsByMovieId(movieId) } throws AflamiException()

        assertThrows<AflamiException> {
            getMovieCastUseCase(movieId)
        }
        coVerify(exactly = 1) { movieRepository.getActorsByMovieId(movieId) }
    }

    @Test
    fun `should handle a negative movie id and return an empty list`() = runTest {

        coEvery { movieRepository.getActorsByMovieId(invalidMovieId) } returns emptyList()

        val result = getMovieCastUseCase(invalidMovieId)

        coVerify(exactly = 1) { movieRepository.getActorsByMovieId(invalidMovieId) }
        assertThat(result).isEmpty()
    }

    private val invalidMovieId = -1L
    private val movieId = 1L
    private val actor1 =
        Actor(id = 1, name = "Actor C", imageUrl = "", popularity = 50.0, gender = Gender.Male)
    private val actor2 = Actor(
        id = 2, name = "Actor A", imageUrl = "", popularity = 100.0, gender = Gender.Female
    )
    private val actor3 =
        Actor(id = 3, name = "Actor B", imageUrl = "", popularity = 75.0, gender = Gender.Male)
}