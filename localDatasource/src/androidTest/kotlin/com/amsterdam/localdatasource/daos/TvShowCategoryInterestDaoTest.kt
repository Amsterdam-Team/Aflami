package com.amsterdam.localdatasource.daos

import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.repository.dto.local.LocalTvShowCategoryInterestDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TvShowCategoryInterestDaoTest : BaseDaoTest() {
    private lateinit var interestDao: TvShowCategoryInterestDao

    @BeforeEach
    fun setup() {
        interestDao = aflamiDatabase.tvShowCategoryInterestDao()
    }

    @Test
    fun upsertInterest_shouldAddNewInterest() = runTest {
        interestDao.upsertInterest(initialTvShowCategoryInterestDto)
        val stored = interestDao.getInterestCount(initialTvShowCategoryInterestDto.categoryId)

        assertThat(stored).isEqualTo(initialTvShowCategoryInterestDto.interestCount)
    }

    @Test
    fun upsertInterest_shouldUpdateExistingInterest() = runTest {
        interestDao.upsertInterest(initialTvShowCategoryInterestDto)
        interestDao.upsertInterest(updatedTvShowCategoryInterestDto)
        val stored = interestDao.getInterestCount(initialTvShowCategoryInterestDto.categoryId)

        assertThat(stored).isEqualTo(updatedTvShowCategoryInterestDto.interestCount)
    }

    @Test
    fun getInterestCount_shouldReturnCorrectCount() = runTest {
        interestDao.upsertInterest(initialTvShowCategoryInterestDto)

        val count = interestDao.getInterestCount(initialTvShowCategoryInterestDto.categoryId)

        assertThat(count).isEqualTo(initialTvShowCategoryInterestDto.interestCount)
    }

    @Test
    fun getInterestCount_shouldReturnNull_whenNotStored() = runTest {
        val count = interestDao.getInterestCount(1)

        assertThat(count).isNull()
    }

    @Test
    fun incrementInterest_shouldAddNewRecord_ifNotExist() = runTest {
        interestDao.incrementInterest(1)
        val stored = interestDao.getInterestCount(1)

        assertThat(stored).isEqualTo(1)
    }

    @Test
    fun incrementInterest_shouldIncrementExistingRecord() = runTest {
        interestDao.upsertInterest(initialTvShowCategoryInterestDto)

        interestDao.incrementInterest(initialTvShowCategoryInterestDto.categoryId)
        val count = interestDao.getInterestCount(initialTvShowCategoryInterestDto.categoryId)

        assertThat(count).isEqualTo(initialTvShowCategoryInterestDto.interestCount + 1)
    }
}

private val initialTvShowCategoryInterestDto = LocalTvShowCategoryInterestDto(1, interestCount = 1)

private val updatedTvShowCategoryInterestDto = LocalTvShowCategoryInterestDto(1, interestCount = 5)