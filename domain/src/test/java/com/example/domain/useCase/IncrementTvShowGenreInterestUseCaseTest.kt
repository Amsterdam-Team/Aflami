package com.example.domain.useCase

import com.example.domain.repository.TvShowRepository
import com.example.entity.category.TvShowGenre
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IncrementTvShowGenreInterestUseCaseTest {

    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var useCase: IncrementTvShowGenreInterestUseCase

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        useCase = IncrementTvShowGenreInterestUseCase(tvShowRepository)
    }

    @Test
    fun `should call repository to increment TV show genre interest`() = runTest {
        // Given
        val genre = TvShowGenre.COMEDY
        coEvery { tvShowRepository.incrementGenreInterest(genre) } returns Unit

        // When
        useCase(genre)

        // Then
        coVerify(exactly = 1) { tvShowRepository.incrementGenreInterest(genre) }
    }
}
