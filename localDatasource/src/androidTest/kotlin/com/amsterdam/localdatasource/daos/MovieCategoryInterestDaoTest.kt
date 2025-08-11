package com.amsterdam.localdatasource.daos

import com.amsterdam.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.amsterdam.repository.dto.local.LocalMovieCategoryInterestDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieCategoryInterestDaoTest : BaseDaoTest() {
    private lateinit var interestDao: MovieCategoryInterestDao

    @BeforeEach
    fun setup() {
        interestDao = aflamiDatabase.movieCategoryInterestDao()
    }

    @Test
    fun upsertInterest_shouldAddNewInterest() = runTest {
        interestDao.upsertInterest(localMovieCategoryInterestDto)
        val stored = interestDao.getInterestCount(localMovieCategoryInterestDto.categoryId)

        assertThat(stored).isEqualTo(localMovieCategoryInterestDto.interestCount)
    }

    @Test
    fun upsertInterest_shouldUpdateExistingInterest() = runTest {
        interestDao.upsertInterest(localMovieCategoryInterestDto)
        interestDao.upsertInterest(updatedLocalMovieCategoryInterestDto)
        val stored = interestDao.getInterestCount(localMovieCategoryInterestDto.categoryId)

        assertThat(stored).isEqualTo(updatedLocalMovieCategoryInterestDto.interestCount)
    }

    @Test
    fun getInterestCount_shouldReturnCorrectCount() = runTest {
        interestDao.upsertInterest(localMovieCategoryInterestDto)

        val count = interestDao.getInterestCount(localMovieCategoryInterestDto.categoryId)

        assertThat(count).isEqualTo(localMovieCategoryInterestDto.interestCount)
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
        interestDao.upsertInterest(localMovieCategoryInterestDto)

        interestDao.incrementInterest(localMovieCategoryInterestDto.categoryId)
        val count = interestDao.getInterestCount(localMovieCategoryInterestDto.categoryId)

        assertThat(count).isEqualTo(localMovieCategoryInterestDto.interestCount + 1)
    }
}

private val localMovieCategoryInterestDto = LocalMovieCategoryInterestDto(
    interestCount = 1,
    categoryId = 123
)

private val updatedLocalMovieCategoryInterestDto = LocalMovieCategoryInterestDto(
    interestCount = 5,
    categoryId = 123
)