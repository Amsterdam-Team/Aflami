package com.example.domain.useCase

import com.example.domain.repository.MovieRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMoviesByActorUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMoviesByActorUseCase: GetMoviesByActorUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMoviesByActorUseCase = GetMoviesByActorUseCase(movieRepository)
    }

    @Test
    fun `should call getMoviesByActor from movieRepository`() =
        runBlocking {
            getMoviesByActorUseCase("actorName")
            coVerify { movieRepository.getMoviesByActor(any()) }
        }
}
