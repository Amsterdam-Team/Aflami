package com.example.domain.useCase

import com.example.domain.repository.TvShowRepository
import com.example.entity.TvShow
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTvShowByKeywordUseCaseTest {
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getTvShowByKeywordUseCase: GetTvShowByKeywordUseCase
    private val fakeTvShowList =
        listOf(
            TvShow(
                id = 1,
                name = "abc",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 2.5f,
                popularity = 10.2,
            ),
            TvShow(
                id = 2,
                name = "dfg",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 2.5f,
                popularity = 11.2,
            ),
            TvShow(
                id = 3,
                name = "hij",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 2.5f,
                popularity = 0.2,
            ),
        )

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getTvShowByKeywordUseCase = GetTvShowByKeywordUseCase(tvShowRepository)
    }

    @Test
    fun `should call getTvShowByKeyword from tvShowRepository`() =
        runBlocking {
            coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowList

            getTvShowByKeywordUseCase("keyword")
            coVerify { tvShowRepository.getTvShowByKeyword("keyword") }
        }

    @Test
    fun `should return a list of tv shows sorted by popularity`() =
        runBlocking {
            coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowList
            val tvShows = getTvShowByKeywordUseCase("keyword")
            assertThat(fakeTvShowList.sortedByDescending { it.popularity }).isEqualTo(tvShows)
        }
}
