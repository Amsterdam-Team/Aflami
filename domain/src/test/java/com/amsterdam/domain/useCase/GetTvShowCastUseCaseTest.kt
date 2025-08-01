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
    fun `should call getTvShowCast with the correct tvShowId`() = runTest {
        // Given
        val tvShowId = 1L
        coEvery { tvShowRepository.getTvShowCast(tvShowId) } returns emptyList()

        // When
        getTvShowCastUseCase(tvShowId)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowCast(tvShowId) }
    }

    @Test
    fun `should return actors sorted by popularity in descending order`() = runTest {
        // Given
        val tvShowId = 1L
        val actor1 =
            Actor(id = 1, name = "A", imageUrl = "", popularity = 50.0, gender = Gender.Male)
        val actor2 =
            Actor(id = 2, name = "B", imageUrl = "", popularity = 51.0, gender = Gender.Male)
        val actor3 =
            Actor(id = 3, name = "C", imageUrl = "", popularity = 52.0, gender = Gender.Female)
        val unsortedActors = listOf(actor1, actor2, actor3)
        val expectedSortedActors = listOf(actor3, actor2, actor1)

        coEvery { tvShowRepository.getTvShowCast(tvShowId) } returns unsortedActors

        // When
        val result = getTvShowCastUseCase(tvShowId)

        // Then
        assertThat(result).isEqualTo(expectedSortedActors)
        assertThat(result).containsExactly(actor3, actor2, actor1).inOrder()
    }

    @Test
    fun `should return an empty list when repository returns an empty list`() = runTest {
        // Given
        val tvShowId = 1L
        coEvery { tvShowRepository.getTvShowCast(tvShowId) } returns emptyList()

        // When
        val result = getTvShowCastUseCase(tvShowId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw AflamiException when repository call fails`() = runTest {
        // Given
        val tvShowId = 1L
        coEvery { tvShowRepository.getTvShowCast(tvShowId) } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> { getTvShowCastUseCase(tvShowId) }
        coVerify(exactly = 1) { tvShowRepository.getTvShowCast(tvShowId) }
    }

    @Test
    fun `should handle a negative tv show id and return an empty list`() = runTest {
        // Given
        val invalidTvShowId = -1L
        coEvery { tvShowRepository.getTvShowCast(invalidTvShowId) } returns emptyList()

        // When
        val result = getTvShowCastUseCase(invalidTvShowId)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowCast(invalidTvShowId) }
        assertThat(result).isEmpty()
    }
}