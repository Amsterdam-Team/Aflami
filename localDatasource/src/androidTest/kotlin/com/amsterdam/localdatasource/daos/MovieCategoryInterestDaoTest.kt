package com.amsterdam.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.amsterdam.repository.dto.local.LocalMovieCategoryInterestDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieCategoryInterestDaoTest {

    private lateinit var database: AflamiDatabase
    private lateinit var interestDao: MovieCategoryInterestDao

    @BeforeEach
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AflamiDatabase::class.java).build()
        interestDao = database.movieCategoryInterestDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertInterest_shouldAddNewInterest() =
        runTest {
            // Given
            val dto = LocalMovieCategoryInterestDto(
                interestCount = 1,
                categoryId = 123
            )

            // When
            interestDao.upsertInterest(dto)

            // Then
            val stored = interestDao.getInterestCount(dto.categoryId)
            assertThat(stored).isEqualTo(1)
        }

    @Test
    fun upsertInterest_shouldUpdateExistingInterest() =
        runTest {
            // Given
            val dto = LocalMovieCategoryInterestDto(
                interestCount = 1,
                categoryId = 123
            )

            val initial = LocalMovieCategoryInterestDto(dto.categoryId, interestCount = 1)
            interestDao.upsertInterest(initial)

            val updated = LocalMovieCategoryInterestDto(dto.categoryId, interestCount = 5)

            // When
            interestDao.upsertInterest(updated)

            // Then
            val stored = interestDao.getInterestCount(dto.categoryId)
            assertThat(stored).isEqualTo(5)
        }

    @Test
    fun getInterestCount_shouldReturnCorrectCount() =
        runTest {
            // Given
            val categoryId = 1L
            interestDao.upsertInterest(LocalMovieCategoryInterestDto(categoryId, interestCount = 4))

            // When
            val count = interestDao.getInterestCount(categoryId)

            // Then
            assertThat(count).isEqualTo(4)
        }

    @Test
    fun getInterestCount_shouldReturnNull_whenNotStored() = runTest {
        // When
        val count = interestDao.getInterestCount(1)

        // Then
        assertThat(count).isNull()
    }

    @Test
    fun incrementInterest_shouldAddNewRecord_ifNotExist() = runTest {
        // When
        interestDao.incrementInterest(1)

        // Then
        val stored = interestDao.getInterestCount(1)
        assertThat(stored).isEqualTo(1)
    }

    @Test
    fun incrementInterest_shouldIncrementExistingRecord() =
        runTest {
            // Given
            interestDao.upsertInterest(LocalMovieCategoryInterestDto(1, 2))

            // When
            interestDao.incrementInterest(1)

            // Then
            val count = interestDao.getInterestCount(1)
            assertThat(count).isEqualTo(3)
        }

    @Test
    fun getAllInterests_shouldReturnAllInsertedItems() = runTest {
        // Given
        val list = listOf(
            LocalMovieCategoryInterestDto(1, 1),
            LocalMovieCategoryInterestDto(1, 3),
        )
        list.forEach { interestDao.upsertInterest(it) }

        // When
        val stored = interestDao.getInterestCount(1)

        // Then
        assertThat(stored).isEqualTo(4)
    }
}
