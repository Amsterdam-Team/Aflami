package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.localdatasource.utils.createLocalTvShowCategoryDto
import com.amsterdam.localdatasource.utils.createTvShow
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TvShowLocalDataSourceImplTest {

    private lateinit var tvShowDao: TvShowDao
    private lateinit var tvShowCategoryInterestDao: TvShowCategoryInterestDao
    private lateinit var dataSource: TvShowLocalDataSourceImpl

    @BeforeEach
    fun setUp() {
        tvShowDao = mockk(relaxed = true)
        tvShowCategoryInterestDao = mockk(relaxed = true)
        dataSource = TvShowLocalDataSourceImpl(tvShowDao, tvShowCategoryInterestDao)
    }

    @Test
    fun `getTvShowsByKeywordAndSearchType should return correct list`() = runTest {
        // Given
        val expected = listOf(mockk<TvShowWithCategory>())
        coEvery {
            tvShowDao.getTvShowsBySearchKeywordSortedByInterest(
                keyword = "action",
                storedLanguage = "en",
                limit = 10,
                offset = 0
            )
        } returns expected

        // When
        val result = dataSource.getTvShowsBySearchKeywordSortedByInterest(
            searchKeyword = "action",
            storedLanguage = "en",
            limit = 10,
            offset = 0
        )

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `addTvShows should insert shows and insert tv show search object`() = runTest {
        // Given
        val shows = listOf(
            createTvShow(id = 1, name = "Test Show", language = "en"),
        )
        val language = "en"
        val keyword = "test"

        val expectedMappings = shows.map {
            SearchTvShowCrossRefDto(
                tvShowId = it.tvShowId,
                storedLanguage = language,
                searchKeyword = keyword
            )
        }

        // When
        dataSource.addTvShows(shows,keyword,language)

        // Then
        coVerify { tvShowDao.addAllTvShows(shows) }

        coVerify { tvShowDao.insertTvShowSearchEntries(expectedMappings) }
    }

    @Test
    fun `incrementGenreInterest should call dao`() = runTest {
        // Given
        val categoryId = 1L

        // When
        dataSource.incrementGenreInterest(categoryId)

        // Then
        coVerify { tvShowCategoryInterestDao.incrementInterest(categoryId) }
    }

    @Test
    fun `addTvShowWithCategories should insert the tvShow and corresponding category cross-references`()=
        runTest {
            // Given
            val tvShow = createTvShow(id = 1L, language = "en")
            val categories = listOf(createLocalTvShowCategoryDto(categoryId = 1L))
            val expectedCrossRefs = listOf(
                TvShowCategoryCrossRefDto(
                    tvShowId = tvShow.tvShowId,
                    categoryId = categories.first().categoryId,
                    storedLanguage = "en"
                )
            )

            // When
            dataSource.addTvShowWithCategories(tvShow, categories, "en")

            // Then
            coVerify(exactly = 1) { tvShowDao.insertTvShow(tvShow) }
            coVerify(exactly = 1) { tvShowDao.insertTvShowCategoryCrossRefs(expectedCrossRefs) }

        }
}
