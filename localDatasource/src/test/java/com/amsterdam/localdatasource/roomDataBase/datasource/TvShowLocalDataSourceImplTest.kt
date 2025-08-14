package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.repository.dto.local.TvShowCategoryCrossRefDto
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategories
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowLocalDataSourceImplTest {

    private val tvShowDao by lazy { mockk<TvShowDao>(relaxed = true) }
    private val tvShowCategoryInterestDao by lazy { mockk<TvShowCategoryInterestDao>(relaxed = true) }
    private val dataSource by lazy {
        TvShowLocalDataSourceImpl(
            tvShowDao,
            tvShowCategoryInterestDao
        )
    }

    @Test
    fun `getTvShowById should return the correct tvShow`() = runTest {
        coEvery { dataSource.getTvShowById(tvShow.tvShowId, tvShow.storedLanguage) } returns tvShow

        val result = dataSource.getTvShowById(tvShow.tvShowId, tvShow.storedLanguage)

        assertThat(result).isEqualTo(tvShow)
    }

    @Test
    fun `incrementGenreInterest should call incrementInterest in the interestDao with correct genre`() =
        runTest {
            dataSource.incrementGenreInterest(categoryId)

            coVerify(exactly = 1) { tvShowCategoryInterestDao.incrementInterest(categoryId) }
        }

    @Test
    fun `upsertTvShowWithCategories should call upsertTvShow and upsertTvShowCategoryCrossRefs when called`() =
        runTest {
            dataSource.upsertTvShowWithCategories(tvShow, categories, storedLanguage)

            coVerify(exactly = 1) { tvShowDao.upsertTvShow(tvShow) }
            coVerify(exactly = 1) { tvShowDao.upsertTvShowCategoryCrossRefs(tvShowCategoryCrossRefs) }
        }

    @Test
    fun `upsertTvShow should call upsertTvShow in the TvShowDao with provided tvShow`() = runTest {
        dataSource.upsertTvShow(tvShow)

        coVerify(exactly = 1) { dataSource.upsertTvShow(tvShow) }
    }

    @Test
    fun `getPopularTvShows should return tvShowsWithCategories when called and tvShowDao returns tvShowsWithCategories`() =
        runTest {
            coEvery { tvShowDao.getPopularTvShows(storedLanguage) } returns tvShowsWithCategories

            val result = dataSource.getPopularTvShows(storedLanguage)

            assertThat(result).isEqualTo(tvShowsWithCategories)
        }


    @Test
    fun `getTopRatedTvShows should return localTvShows when called and tvShowDao returns localTvShows`() =
        runTest {
            coEvery { tvShowDao.getTopRatedTvShows(storedLanguage) } returns tvShows

            val result = dataSource.getTopRatedTvShows(storedLanguage)

            assertThat(result).isEqualTo(tvShows)
        }

    @Test
    fun `upsertPopularTvShows should call insertPopularTvShows in the tvShowDao`() = runTest {
        dataSource.upsertPopularTvShows(tvShows)

        coVerify(exactly = 1) {
            tvShowDao.upsertPopularTvShows(match { list ->
                list.size == 1 &&
                        list[0].tvShowId == tvShows[0].tvShowId &&
                        list[0].storedLanguage == tvShows[0].storedLanguage
            })
        }
    }

    @Test
    fun `upsertTopRatedTvShows should call insertTopRatedTvShows in the tvShowDao`() = runTest {
        dataSource.upsertTopRatedTvShows(tvShows)

        coVerify(exactly = 1) {
            tvShowDao.upsertTopRatedTvShows(match { list ->
                list.size == tvShows.size &&
                        list[0].tvShowId == tvShows[0].tvShowId &&
                        list[0].storedLanguage == tvShows[0].storedLanguage
            })
        }
    }


    @Test
    fun `deleteExpiredPopularTvShows should call deleteExpiredPopularTvShows in the tvShowDao`() =
        runTest {
            dataSource.deleteExpiredPopularTvShows(expirationTime, storedLanguage)

            coVerify(exactly = 1) {
                tvShowDao.deleteExpiredPopularTvShows(
                    expirationTime,
                    storedLanguage
                )
            }
        }

    @Test
    fun `deleteAllExpiredTopRatedTvShows should call deleteAllExpiredTopRatedTvShows in the tvShowDao`() =
        runTest {
            dataSource.deleteExpiredTopRatedTvShows(expirationTime, storedLanguage)

            coVerify(exactly = 1) {
                tvShowDao.deleteExpiredTopRatedTvShows(
                    expirationTime,
                    storedLanguage
                )
            }
        }
}

private const val storedLanguage = "en"

private val expirationTime = Instant.parse("2023-01-01T00:00:00Z")

private val tvShow = createTvShow(id = 42, language = storedLanguage)

private const val categoryId = 1L

private val categories = listOf(1L)

private val tvShowCategoryCrossRefs = listOf(
    TvShowCategoryCrossRefDto(
        tvShowId = tvShow.tvShowId,
        categoryId = categories.first(),
        storedLanguage = "en"
    )
)

private val tvShows = listOf(createTvShow(id = 42, language = storedLanguage))

private val tvShowsWithCategories = listOf(
    TvShowWithCategories(
        tvShow = createTvShow(id = 42, language = storedLanguage),
        categories = emptyList()
    )
)

private fun createTvShow(
    id: Long = 1L,
    language: String = "en",
    name: String = "Test Show"
) = TvShowLocalDto(
    tvShowId = id,
    storedLanguage = language,
    name = name,
    description = "Test Description",
    poster = "poster.jpg",
    rating = 8.5f,
    popularity = 123.4,
    airDate = LocalDate.parse("2020-01-01"),
    seasonCount = 5,
    originCountry = "eg"
)