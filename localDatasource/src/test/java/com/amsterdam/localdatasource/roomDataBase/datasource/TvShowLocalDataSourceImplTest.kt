package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.localdatasource.utils.createTvShow
import com.amsterdam.repository.dto.local.TvShowCategoryCrossRefDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategories
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
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
    fun `getTvShowById should return the correct tvShow`() = runTest {
        //Given
        val storedLanguage = "en"
        val tvShow = createTvShow(id = 42, language = storedLanguage)
        coEvery { dataSource.getTvShowById(42, storedLanguage) } returns tvShow
        //When
        val result = dataSource.getTvShowById(42, storedLanguage)
        //Then
        assertThat(result).isEqualTo(tvShow)
    }

    @Test
    fun `incrementGenreInterest should call incrementInterest in the interestDao with correct genre`() =
        runTest {
            //Given
            val categoryId = 1L
            //When
            dataSource.incrementGenreInterest(categoryId)
            //Then
            coVerify(exactly = 1) { tvShowCategoryInterestDao.incrementInterest(categoryId) }
        }

    @Test
    fun `addMovieWithCategories should insert the movie and corresponding category cross-references`() =
        runTest {
            // Given
            val storedLanguage = "en"
            val tvShow = createTvShow(id = 42, language = storedLanguage)
            val categories = listOf(1L)
            val expectedCrossRefs = listOf(
                TvShowCategoryCrossRefDto(
                    tvShowId = tvShow.tvShowId,
                    categoryId = categories.first(),
                    storedLanguage = "en"
                )
            )
            // When
            dataSource.upsertTvShowWithCategories(tvShow, categories, "en")
            // Then
            coVerify(exactly = 1) { tvShowDao.upsertTvShow(tvShow) }
            coVerify(exactly = 1) { tvShowDao.upsertTvShowCategoryCrossRefs(expectedCrossRefs) }
        }

    @Test
    fun `insertTvShow should call insertTvShow in the TvShowDao with provided tvShow`() = runTest {
        //Given
        val storedLanguage = "en"
        val tvShow = createTvShow(id = 42, language = storedLanguage)
        //When
        dataSource.upsertTvShow(tvShow)
        // Then
        coVerify(exactly = 1) { dataSource.upsertTvShow(tvShow) }
    }

    @Test
    fun `getPopularTvShows should return non empty list of tvShows`() = runTest {
        //Given
        val storedLanguage = "en"
        coEvery { tvShowDao.getPopularTvShows(storedLanguage) } returns tvShowsWithCategories
        //When
        val result = dataSource.getPopularTvShows(storedLanguage)
        //Then
        assertThat(result).isEqualTo(tvShowsWithCategories)
    }


    @Test
    fun `getTopRatedTvShows should return non empty list of tvShow`() = runTest {
        //Given
        val storedLanguage = "en"
        coEvery { tvShowDao.getTopRatedTvShows(storedLanguage) } returns localTvShows
        //When
        val result = dataSource.getTopRatedTvShows(storedLanguage)
        //Then
        assertThat(result).isEqualTo(localTvShows)
    }

    @Test
    fun `addPopularTvShows should call insertPopularTvShows in the tvShowDao`() = runTest {
        //Given
        val storedLanguage = "en"
        val tvShows = listOf(createTvShow(id = 42, language = storedLanguage))
        //When
        dataSource.upsertPopularTvShows(tvShows)
        // Then
        coVerify(exactly = 1) {
            tvShowDao.upsertPopularTvShows(
                match { list ->
                    list.size == 1 &&
                        list[0].tvShowId == 42L &&
                        list[0].storedLanguage == "en"
            }
                    )
        }
    }

    @Test
    fun `addTopRatedTvShows should call insertTopRatedTvShows in the tvShowDao`() = runTest {
        //Given
        val storedLanguage = "en"
        val tvShows = listOf(createTvShow(id = 42, language = storedLanguage))        //When
        dataSource.upsertTopRatedTvShows(tvShows)
        // Then
        coVerify(exactly = 1) {
            tvShowDao.upsertTopRatedTvShows(match { list ->
                list.size == 1 &&
                        list[0].tvShowId == 42L &&
                        list[0].storedLanguage == "en"
            })
        }
    }


    @Test
    fun `deleteExpiredPopularTvShows should call deleteExpiredPopularTvShows`() = runTest {
        //Given
        val expirationTime = Instant.parse("2023-01-01T00:00:00Z")
        val storedLanguage = "en"
        //When
        dataSource.deleteExpiredPopularTvShows(expirationTime, storedLanguage)
        //Then
        coVerify(exactly = 1) {
            tvShowDao.deleteExpiredPopularTvShows(
                expirationTime,
                storedLanguage
            )
        }
    }

    @Test
    fun `deleteAllExpiredTopRatedTvShows should call deleteAllExpiredTopRatedTvShows`() = runTest {
        //Given
        val expirationTime = Instant.parse("2023-01-01T00:00:00Z")
        val storedLanguage = "en"
        //When
        dataSource.deleteExpiredTopRatedTvShows(expirationTime, storedLanguage)
        //Then
        coVerify(exactly = 1) {
            tvShowDao.deleteExpiredTopRatedTvShows(
                expirationTime,
                storedLanguage
            )
        }
    }

    val localTvShows = listOf(createTvShow(id = 42, language = "en"))
    val tvShowsWithCategories = listOf(
        TvShowWithCategories(
            tvShow = createTvShow(id = 42, language = "en"),
            categories = emptyList()
        )
    )

}