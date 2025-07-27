package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.details.GetTvShowCastUseCase
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

class GetTvShowCastUseCaseTest {
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getTvShowCastUseCase: GetTvShowCastUseCase

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getTvShowCastUseCase = GetTvShowCastUseCase(tvShowRepository)
    }

    @Test
    fun `should call getTvShowCast and return actors sorted by popularity`() = runTest {
        val tvShowId = 1L
        coEvery { tvShowRepository.getTvShowCast(tvShowId) } returns emptyList()

        getTvShowCastUseCase(tvShowId)

        coVerify { tvShowRepository.getTvShowCast(tvShowId) }
    }

    @Test
    fun `should return an empty list when getTvShowCast returns empty`() = runTest {
        val tvShowId = 1L
        coEvery { tvShowRepository.getTvShowCast(tvShowId) } returns emptyList()

        val result = getTvShowCastUseCase(tvShowId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return a list of actors when getTvShowCast returns list successfully `() = runTest {
        val tvShowId = 1L
        val actor1 = Actor(id = 1, name = "A", imageUrl = "", popularity = 50.0, gender = Gender.Male)
        val actor2 = Actor(id = 2, name = "B", imageUrl = "", popularity = 51.0, gender = Gender.Male)
        val actor3 = Actor(id = 3, name = "C", imageUrl = "", popularity = 52.0, gender = Gender.Female)
        coEvery { tvShowRepository.getTvShowCast(tvShowId) } returns listOf(actor1, actor2, actor3)

        val result = getTvShowCastUseCase(tvShowId)

        assertThat(result).containsExactly(actor2, actor3, actor1)
    }

    @Test
    fun `should throw AflamiException when getActorsByMovieId fails`() = runTest {
        val tvShowId = 1L
        coEvery { tvShowRepository.getTvShowCast(tvShowId) } throws AflamiException()

        assertThrows<AflamiException> { getTvShowCastUseCase(tvShowId) }
    }
}