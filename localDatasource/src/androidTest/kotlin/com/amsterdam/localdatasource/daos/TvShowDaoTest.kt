package com.amsterdam.localdatasource.daos

import com.amsterdam.localdatasource.roomDataBase.daos.CategoryDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.PopularTvShowDto
import com.amsterdam.repository.dto.local.TopRatedTvShowDto
import com.amsterdam.repository.dto.local.TvShowCategoryCrossRefDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategories
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.days

class TvShowDaoTest : BaseDaoTest() {
    private lateinit var tvShowDao: TvShowDao
    private lateinit var categoryDao: CategoryDao

    @BeforeEach
    fun setUp() {
        tvShowDao = aflamiDatabase.tvShowDao()
        categoryDao = aflamiDatabase.categoryDao()
    }

    @Test
    fun upsertTvShow_shouldInsertTvShow_whenTvShowNotStored() = runTest {
        tvShowDao.upsertTvShow(tvShow)
        val result = tvShowDao.getTvShowById(tvShow.tvShowId, tvShow.storedLanguage)

        assertThat(result).isEqualTo(tvShow)
    }

    @Test
    fun upsertTvShow_shouldUpdateTvShowWithoutDuplication_whenTvShowAlreadyStored() = runTest {
        tvShowDao.upsertTvShow(tvShow)

        tvShowDao.upsertTvShow(updatedTvShow)
        val result = tvShowDao.getTvShowById(tvShow.tvShowId, tvShow.storedLanguage)

        assertThat(result?.name).isEqualTo(updatedTvShow.name)
    }

    @Test
    fun upsertTvShows_shouldInsertTvShows_whenTvShowsNotStored() = runTest {
        tvShowDao.upsertTvShows(listOf(tvShow))
        val result = tvShowDao.getTvShowById(tvShow.tvShowId, tvShow.storedLanguage)

        assertThat(result).isEqualTo(tvShow)
    }

    @Test
    fun upsertTvShows_shouldUpdateTvShowsWithoutDuplication_whenTvShowsAlreadyStored() = runTest {
        tvShowDao.upsertTvShows(listOf(tvShow))

        tvShowDao.upsertTvShows(listOf(updatedTvShow))
        val result = tvShowDao.getTvShowById(tvShow.tvShowId, tvShow.storedLanguage)

        assertThat(result?.name).isEqualTo(updatedTvShow.name)
    }

    @Test
    fun upsertTvShowsCategoryCrossRefs_shouldInsertsNewCrossRefs_usingRawQuery() = runTest {
        categoryDao.upsertAllTvShowCategories(tvShowCategories)
        tvShowDao.upsertTvShow(tvShow)

        tvShowDao.upsertTvShowCategoryCrossRefs(tvShowsCategoryCrossRefs)
        val cursor = aflamiDatabase.query(
            "SELECT * FROM ${DatabaseConstants.TV_SHOW_CATEGORY_CROSS_REF_TABLE}",
            null
        )
        val retrievedCrossRefs = mutableListOf<TvShowCategoryCrossRefDto>()
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val tvShowId = it.getLong(it.getColumnIndexOrThrow("tvShowId"))
                    val categoryId = it.getLong(it.getColumnIndexOrThrow("categoryId"))
                    val storedLanguage = it.getString(it.getColumnIndexOrThrow("storedLanguage"))
                    retrievedCrossRefs.add(
                        TvShowCategoryCrossRefDto(
                            tvShowId,
                            categoryId,
                            storedLanguage
                        )
                    )
                } while (it.moveToNext())
            }
        }

        assertThat(retrievedCrossRefs).containsExactlyElementsIn(tvShowsCategoryCrossRefs)
    }

    @Test
    fun getPopularTvShows_shouldReturnPopularTvShows_whenTvShowsStored() = runTest {
        tvShowDao.upsertTvShow(tvShow)
        tvShowDao.upsertPopularTvShows(popularTvShows)

        val result = tvShowDao.getPopularTvShows("en")

        assertThat(result).isEqualTo(tvShowWithCategories)
    }

    @Test
    fun getPopularTvShows_shouldReturnEmptyList_whenNoTvShowsStored() = runTest {
        val result = tvShowDao.getPopularTvShows("en")

        assertThat(result).isEmpty()
    }

    @Test
    fun getTopRatedTvShows_shouldReturnTopRatedTvShows_whenTvShowsStored() = runTest {
        tvShowDao.upsertTvShow(tvShow)
        tvShowDao.upsertTopRatedTvShows(topRatedTvShows)

        val result = tvShowDao.getTopRatedTvShows("en")

        assertThat(result).isEqualTo(listOf(tvShow))
    }

    @Test
    fun getTopRatedTvShows_shouldReturnEmptyList_whenNoTvShowsStored() = runTest {
        val result = tvShowDao.getTopRatedTvShows("en")

        assertThat(result).isEmpty()
    }

    @Test
    fun deleteExpiredPopularTvShows_shouldDeleteExpiredPopularTvShows() = runTest {
        tvShowDao.upsertTvShow(tvShow)
        tvShowDao.upsertPopularTvShows(popularTvShows)

        tvShowDao.deleteExpiredPopularTvShows(popularTvShows[0].dateAdded + 5.days, "en")
        val result = tvShowDao.getPopularTvShows("en")

        assertThat(result).isEmpty()
    }

    @Test
    fun deleteExpiredTopRatedTvShows_shouldDeleteExpiredTopRatedTvShows() = runTest {
        tvShowDao.upsertTvShow(tvShow)
        tvShowDao.upsertTopRatedTvShows(topRatedTvShows)

        tvShowDao.deleteExpiredTopRatedTvShows(topRatedTvShows[0].dateAdded + 5.days, "en")
        val result = tvShowDao.getTopRatedTvShows("en")

        assertThat(result).isEmpty()
    }
}

private val tvShow = createTvShow()

private val updatedTvShow = tvShow.copy(name = "Updated")

private val tvShowCategories = listOf(
    TvShowCategoryLocalDto(1L),
    TvShowCategoryLocalDto(2L)
)

private val tvShowsCategoryCrossRefs = listOf(
    TvShowCategoryCrossRefDto(
        tvShowId = 1L, categoryId = 1L,
        storedLanguage = "en"
    ),
    TvShowCategoryCrossRefDto(
        tvShowId = 1L, categoryId = 2L,
        storedLanguage = "en"
    )
)

private val popularTvShows = listOf(
    PopularTvShowDto(1L, "en")
)

private val tvShowWithCategories = listOf(
    TvShowWithCategories(
        tvShow = tvShow,
        categories = emptyList()
    )
)

private val topRatedTvShows = listOf(
    TopRatedTvShowDto(1L, "en")
)

private fun createTvShow(): TvShowLocalDto {
    return TvShowLocalDto(
        tvShowId = 1L,
        storedLanguage = "en",
        name = "Original",
        description = "Test description",
        poster = "poster.jpg",
        airDate = LocalDate.parse("2020-01-01"),
        popularity = 9.5,
        rating = 4.3f,
        originCountry = "US",
        seasonCount = 1
    )
}