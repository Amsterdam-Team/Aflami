package com.example.domain.useCase

import com.example.domain.exceptions.AflamiException
import com.example.domain.repository.TvShowRepository
import com.example.domain.useCase.utils.fakeTvShowList
import com.example.domain.useCase.utils.fakeTvShowListWithCategories
import com.example.domain.useCase.utils.fakeTvShowListWithRatings
import com.example.domain.useCase.utils.specificTvShowList
import com.example.entity.category.TvShowGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetAndFilterTvShowsByKeywordUseCaseTest {
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getAndFilterTvShowsByKeywordUseCase: GetAndFilterTvShowsByKeywordUseCase


    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getAndFilterTvShowsByKeywordUseCase = GetAndFilterTvShowsByKeywordUseCase(tvShowRepository)
    }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should call getTvShowByKeyword one time`() = runTest {
        coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowList

        getAndFilterTvShowsByKeywordUseCase("keyword")
        coVerify { tvShowRepository.getTvShowByKeyword("keyword") }
    }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return a list of tv shows when there is data returned`() =
        runTest {
            coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowList
            val tvShows = getAndFilterTvShowsByKeywordUseCase("keyword")
            assertThat(fakeTvShowList).isEqualTo(tvShows)
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return empty list when no data returned`(): Unit =
        runTest {
            coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns emptyList()
            assertThat(getAndFilterTvShowsByKeywordUseCase("nonexistentKeyword")).isEmpty()
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return empty list when filters yield an empty list`(): Unit =
        runTest {
            coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns specificTvShowList

            assertThat(
                getAndFilterTvShowsByKeywordUseCase(
                    "keyword",
                    rating = 10,
                    tvGenre = TvShowGenre.COMEDY
                )
            ).isEmpty()
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return filtered tv shows when a minimum rating is specified`() = runTest {
        coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowListWithRatings

        val result = getAndFilterTvShowsByKeywordUseCase("keyword", rating = 6)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(1)
    }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return all tv shows when rating filter is 0`() = runTest {
        coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowListWithRatings

        val result = getAndFilterTvShowsByKeywordUseCase("keyword", rating = 0)

        assertThat(result).isEqualTo(fakeTvShowListWithRatings)
    }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return filtered tv shows when a genre is specified`(): Unit = runTest {
        coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowListWithCategories

        val result = getAndFilterTvShowsByKeywordUseCase("keyword", tvGenre = TvShowGenre.COMEDY)

        assertThat(result).hasSize(1)
        assertThat(result).containsExactly(
            fakeTvShowListWithCategories[2],
        )
    }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return all tv shows when genre filter is All`() = runTest {
        coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowListWithCategories

        val result = getAndFilterTvShowsByKeywordUseCase("keyword", tvGenre = TvShowGenre.ALL)

        assertThat(result).isEqualTo(fakeTvShowListWithCategories)
    }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return Aflami exception when an error happened`() = runTest {
        coEvery { tvShowRepository.getTvShowByKeyword(any()) } throws AflamiException()
        assertThrows<AflamiException> { getAndFilterTvShowsByKeywordUseCase("keyword") }
    }
}
