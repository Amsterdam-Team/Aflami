package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.search.GetAndFilterTvShowsByKeywordUseCase
import com.amsterdam.domain.useCase.utils.fakeTvShowList
import com.amsterdam.domain.useCase.utils.fakeTvShowListWithCategories
import com.amsterdam.domain.useCase.utils.fakeTvShowListWithRatings
import com.amsterdam.domain.useCase.utils.specificTvShowList
import com.amsterdam.entity.category.TvShowGenre
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
        coEvery {
            tvShowRepository.getTvShowByKeyword(
                any(),
                page = 1,
                tvShowsPerPage = 20
            )
        } returns fakeTvShowList
        getAndFilterTvShowsByKeywordUseCase("keyword")
        coVerify(exactly = 1) {
            tvShowRepository.getTvShowByKeyword(
                "keyword",
                page = 1,
                tvShowsPerPage = 20
            )
        }
    }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return a list of tv shows when there is data returned`() =
        runTest {
            coEvery {
                tvShowRepository.getTvShowByKeyword(
                    any(),
                    page = 1,
                    tvShowsPerPage = 20
                )
            } returns fakeTvShowList
            val tvShows = getAndFilterTvShowsByKeywordUseCase("keyword")
            assertThat(tvShows).isEqualTo(fakeTvShowList)
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return empty list when no data returned`() =
        runTest {
            coEvery {
                tvShowRepository.getTvShowByKeyword(
                    any(),
                    page = 1,
                    tvShowsPerPage = 20
                )
            } returns emptyList()
            assertThat(getAndFilterTvShowsByKeywordUseCase("nonexistentKeyword")).isEmpty()
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return empty list when filters yield an empty list`() =
        runTest {
            coEvery {
                tvShowRepository.getTvShowByKeyword(
                    any(),
                    page = 1,
                    tvShowsPerPage = 20
                )
            } returns specificTvShowList

            val result = getAndFilterTvShowsByKeywordUseCase(
                "keyword",
                rating = 10,
                tvGenre = TvShowGenre.COMEDY
            )
            assertThat(result).isEmpty()
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return filtered tv shows when a minimum rating is specified`() =
        runTest {
            coEvery {
                tvShowRepository.getTvShowByKeyword(
                    any(),
                    page = 1,
                    tvShowsPerPage = 20
                )
            } returns fakeTvShowListWithRatings

            val result = getAndFilterTvShowsByKeywordUseCase("keyword", rating = 6)

            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo(1)
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return all tv shows when rating filter is 0`() =
        runTest {
            coEvery {
                tvShowRepository.getTvShowByKeyword(
                    any(),
                    page = 1,
                    tvShowsPerPage = 20
                )
            } returns fakeTvShowListWithRatings

            val result = getAndFilterTvShowsByKeywordUseCase("keyword", rating = 0)

            assertThat(result).isEqualTo(fakeTvShowListWithRatings)
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return filtered tv shows when a genre is specified`() =
        runTest {
            coEvery {
                tvShowRepository.getTvShowByKeyword(
                    any(),
                    page = 1,
                    tvShowsPerPage = 20
                )
            } returns fakeTvShowListWithCategories

            val result =
                getAndFilterTvShowsByKeywordUseCase("keyword", tvGenre = TvShowGenre.COMEDY)

            assertThat(result).hasSize(1)
            assertThat(result).containsExactly(fakeTvShowListWithCategories[2])
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return all tv shows when genre filter is All`() =
        runTest {
            coEvery {
                tvShowRepository.getTvShowByKeyword(
                    any(),
                    page = 1,
                    tvShowsPerPage = 20
                )
            } returns fakeTvShowListWithCategories

            val result = getAndFilterTvShowsByKeywordUseCase("keyword", tvGenre = TvShowGenre.ALL)

            assertThat(result).isEqualTo(fakeTvShowListWithCategories)
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should throw Aflami exception when an error happened`() =
        runTest {
            coEvery {
                tvShowRepository.getTvShowByKeyword(
                    any(),
                    page = 1,
                    tvShowsPerPage = 20
                )
            } throws AflamiException()
            assertThrows<AflamiException> { getAndFilterTvShowsByKeywordUseCase("keyword") }
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should filter by both rating and genre`() = runTest {
        coEvery {
            tvShowRepository.getTvShowByKeyword(
                any(),
                any(),
                any()
            )
        } returns fakeTvShowListWithCategories

        val result =
            getAndFilterTvShowsByKeywordUseCase("keyword", rating = 7, tvGenre = TvShowGenre.COMEDY)

        assertThat(result).hasSize(1)
        assertThat(result).containsExactly(fakeTvShowListWithCategories[2])
    }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should pass correct pagination parameters`() =
        runTest {
            val keyword = "test"
            val page = 2
            val tvShowsPerPage = 10

            coEvery {
                tvShowRepository.getTvShowByKeyword(
                    keyword = keyword,
                    page = page,
                    tvShowsPerPage = tvShowsPerPage
                )
            } returns emptyList()

            getAndFilterTvShowsByKeywordUseCase(keyword, page, tvShowsPerPage)

            coVerify(exactly = 1) {
                tvShowRepository.getTvShowByKeyword(
                    keyword = keyword,
                    page = page,
                    tvShowsPerPage = tvShowsPerPage
                )
            }
        }
}