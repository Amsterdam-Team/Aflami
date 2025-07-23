package com.example.domain.useCase

import com.example.domain.exceptions.AflamiException
import com.example.domain.repository.TvShowRepository
import com.example.domain.useCase.utils.fakeTvShowList
import com.example.domain.useCase.utils.fakeTvShowListWithCategories
import com.example.domain.useCase.utils.fakeTvShowListWithRatings
import com.example.domain.useCase.utils.specificTvShowList
import com.example.entity.TvShow
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
        coEvery {
            tvShowRepository.getTvShowByKeyword(
                any(),
                page = 1,
                tvShowsPerPage = 20
            )
        } returns fakeTvShowList

        getAndFilterTvShowsByKeywordUseCase("keyword")
        coVerify {
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
            assertThat(fakeTvShowList).isEqualTo(tvShows)
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should return empty list when no data returned`(): Unit =
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
    fun `getAndFilterTvShowsByKeywordUseCase should return empty list when filters yield an empty list`(): Unit =
        runTest {
            coEvery {
                tvShowRepository.getTvShowByKeyword(
                    any(),
                    page = 1,
                    tvShowsPerPage = 20
                )
            } returns specificTvShowList

            assertThat(
                getAndFilterTvShowsByKeywordUseCase(
                    "keyword",
                    rating = 10,
                    tvGenre = TvShowGenre.COMEDY
                )
            ).isEmpty()
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
    fun `getAndFilterTvShowsByKeywordUseCase should return filtered tv shows when a genre is specified`(): Unit =
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
            assertThat(result).containsExactly(
                fakeTvShowListWithCategories[2],
            )
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
    fun `getAndFilterTvShowsByKeywordUseCase should return Aflami exception when an error happened`() =
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
    fun `getAndFilterTvShowsByKeywordUseCase should throw AflamiException when getAllGenreInterests fails`() =
        runTest {
            coEvery {
                tvShowRepository.getTvShowByKeyword(
                    any(),
                    any(),
                    any()
                )
            } returns fakeTvShowList
            coEvery { tvShowRepository.getAllGenreInterests() } throws AflamiException()

            assertThrows<AflamiException> { getAndFilterTvShowsByKeywordUseCase("keyword") }
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should sort tv shows by user interest in categories`() =
        runTest {
            val tvShow1 = TvShow(
                id = 1,
                name = "Sci-Fi Show",
                description = "",
                posterUrl = "",
                productionYear = (2023).toUInt(),
                categories = listOf(TvShowGenre.SCIENCE_FICTION_FANTASY),
                rating = 7.0f,
                popularity = 10.0
            )
            val tvShow2 = TvShow(
                id = 2,
                name = "Drama Series",
                description = "",
                posterUrl = "",
                productionYear = (2023).toUInt(),
                categories = listOf(TvShowGenre.DRAMA),
                rating = 8.0f,
                popularity = 12.0
            )
            val tvShow3 = TvShow(
                id = 3,
                name = "Kids Comedy",
                description = "",
                posterUrl = "",
                productionYear = (2023).toUInt(),
                categories = listOf(TvShowGenre.KIDS, TvShowGenre.COMEDY),
                rating = 7.5f,
                popularity = 9.0
            )

            val tvShowsFromRepo = listOf(tvShow1, tvShow2, tvShow3)

            coEvery { tvShowRepository.getAllGenreInterests() } returns mapOf(
                TvShowGenre.KIDS to 100,
                TvShowGenre.DRAMA to 80,
                TvShowGenre.COMEDY to 50,
                TvShowGenre.SCIENCE_FICTION_FANTASY to 70
            )

            coEvery {
                tvShowRepository.getTvShowByKeyword(
                    any(),
                    any(),
                    any()
                )
            } returns tvShowsFromRepo

            val result = getAndFilterTvShowsByKeywordUseCase("keyword")

            assertThat(result).containsExactly(tvShow3, tvShow2, tvShow1).inOrder()
        }

    @Test
    fun `getAndFilterTvShowsByKeywordUseCase should handle tv shows with empty categories in sorting`() =
        runTest {
            val tvShowWithEmptyCategories = TvShow(
                id = 4,
                name = "No Category Show",
                description = "",
                posterUrl = "",
                productionYear = (2023).toUInt(),
                categories = emptyList(),
                rating = 6.0f,
                popularity = 5.0
            )
            val tvShowWithCategories = TvShow(
                id = 5,
                name = "Has Category Show",
                description = "",
                posterUrl = "",
                productionYear = (2023).toUInt(),
                categories = listOf(TvShowGenre.DRAMA),
                rating = 7.0f,
                popularity = 8.0
            )

            coEvery { tvShowRepository.getAllGenreInterests() } returns mapOf(TvShowGenre.DRAMA to 50)
            coEvery { tvShowRepository.getTvShowByKeyword(any(), any(), any()) } returns listOf(
                tvShowWithEmptyCategories,
                tvShowWithCategories
            )

            val result = getAndFilterTvShowsByKeywordUseCase("keyword")

            assertThat(result).containsExactly(tvShowWithCategories, tvShowWithEmptyCategories)
                .inOrder()
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
}

