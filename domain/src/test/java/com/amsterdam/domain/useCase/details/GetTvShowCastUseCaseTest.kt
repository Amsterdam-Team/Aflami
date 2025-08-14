package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Gender
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTvShowCastUseCaseTest {
    private val tvShowRepository: TvShowRepository = mockk(relaxed = true)
    private val getTvShowCastUseCase by lazy {
        GetTvShowCastUseCase(tvShowRepository)
    }

    @Test
    fun `should call getTvShowCast with the correct tvShowId`() = runTest {
        coEvery { tvShowRepository.getTvShowCast(tvShowId) } returns emptyList()

        getTvShowCastUseCase(tvShowId)

        coVerify(exactly = 1) { tvShowRepository.getTvShowCast(tvShowId) }
    }

    @Test
    fun `should return actors sorted by popularity in descending order`() = runTest {
        val unsortedActors = listOf(actor1, actor2, actor3)
        coEvery { tvShowRepository.getTvShowCast(tvShowId) } returns unsortedActors

        val result = getTvShowCastUseCase(tvShowId)

        assertThat(result).containsExactly(actor3, actor2, actor1).inOrder()
    }

    @Test
    fun `should return an empty list when repository returns an empty list`() = runTest {
        coEvery { tvShowRepository.getTvShowCast(tvShowId) } returns emptyList()

        val result = getTvShowCastUseCase(tvShowId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository call fails`() = runTest {
        coEvery { tvShowRepository.getTvShowCast(tvShowId) } throws AflamiException()

        assertThrows<AflamiException> { getTvShowCastUseCase(tvShowId) }
    }

    @Test
    fun `should return an empty list when a negative tv show id is given `() = runTest {
        coEvery { tvShowRepository.getTvShowCast(invalidTvShowId) } returns emptyList()

        val result = getTvShowCastUseCase(invalidTvShowId)

        assertThat(result).isEmpty()
    }

    private val invalidTvShowId = -1L
    private val tvShowId = 1L
    private val actor1 =
        Actor(id = 1, name = "A", imageUrl = "", popularity = 50.0, gender = Gender.Male)
    private val actor2 =
        Actor(id = 2, name = "B", imageUrl = "", popularity = 51.0, gender = Gender.Male)
    private val actor3 =
        Actor(id = 3, name = "C", imageUrl = "", popularity = 52.0, gender = Gender.Female)
}