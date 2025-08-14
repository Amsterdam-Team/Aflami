package com.amsterdam.localdatasource.daos

import com.amsterdam.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.amsterdam.repository.dto.local.MovieCategoryInterestLocalDto
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
        interestDao.upsertInterest(movieCategoryInterestLocalDto)
        val stored = interestDao.getInterestCount(movieCategoryInterestLocalDto.categoryId)

        assertThat(stored).isEqualTo(movieCategoryInterestLocalDto.interestCount)
    }

    @Test
    fun upsertInterest_shouldUpdateExistingInterest() = runTest {
        interestDao.upsertInterest(movieCategoryInterestLocalDto)
        interestDao.upsertInterest(updatedMovieCategoryInterestLocalDto)
        val stored = interestDao.getInterestCount(movieCategoryInterestLocalDto.categoryId)

        assertThat(stored).isEqualTo(updatedMovieCategoryInterestLocalDto.interestCount)
    }

    @Test
    fun getInterestCount_shouldReturnCorrectCount() = runTest {
        interestDao.upsertInterest(movieCategoryInterestLocalDto)

        val count = interestDao.getInterestCount(movieCategoryInterestLocalDto.categoryId)

        assertThat(count).isEqualTo(movieCategoryInterestLocalDto.interestCount)
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
        interestDao.upsertInterest(movieCategoryInterestLocalDto)

        interestDao.incrementInterest(movieCategoryInterestLocalDto.categoryId)
        val count = interestDao.getInterestCount(movieCategoryInterestLocalDto.categoryId)

        assertThat(count).isEqualTo(movieCategoryInterestLocalDto.interestCount + 1)
    }
}

private val movieCategoryInterestLocalDto = MovieCategoryInterestLocalDto(
    interestCount = 1,
    categoryId = 123
)

private val updatedMovieCategoryInterestLocalDto = MovieCategoryInterestLocalDto(
    interestCount = 5,
    categoryId = 123
)