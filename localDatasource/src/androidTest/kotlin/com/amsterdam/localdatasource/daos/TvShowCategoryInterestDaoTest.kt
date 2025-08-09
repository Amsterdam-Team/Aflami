package com.amsterdam.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.repository.dto.local.LocalTvShowCategoryInterestDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TvShowCategoryInterestDaoTest {

    private lateinit var database: AflamiDatabase
    private lateinit var interestDao: TvShowCategoryInterestDao

    @BeforeEach
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AflamiDatabase::class.java).build()
        interestDao = database.tvShowCategoryInterestDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }


    @Test
    fun upsertInterest_shouldAddNewInterest() =
        runTest {
            // Given
            val dto = LocalTvShowCategoryInterestDto(1, interestCount = 1)

            // When
            interestDao.upsertInterest(dto)

            //Then
            val stored = interestDao.getInterestCount(1)
            assertThat(stored).isEqualTo(1)
        }

    @Test
    fun upsertInterest_shouldUpdateExistingInterest() =
        runTest {
            // Given
            val initial = LocalTvShowCategoryInterestDto(1, interestCount = 1)
            interestDao.upsertInterest(initial)

            val updated = LocalTvShowCategoryInterestDto(1, interestCount = 5)

            // When
            interestDao.upsertInterest(updated)

            // Then
            val stored = interestDao.getInterestCount(1)
            assertThat(stored).isEqualTo(6)
        }

    @Test
    fun getInterestCount_shouldReturnCorrectCount() =
        runTest {
            // Given
            interestDao.upsertInterest(LocalTvShowCategoryInterestDto(1, interestCount = 3))

            // When
            val count = interestDao.getInterestCount(1)

            // Then
            assertThat(count).isEqualTo(3)
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
            interestDao.upsertInterest(LocalTvShowCategoryInterestDto(1, 2))

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
            LocalTvShowCategoryInterestDto(1, 1),
            LocalTvShowCategoryInterestDto(1, 2),
        )
        list.forEach { interestDao.upsertInterest(it) }

        // When
        val stored = interestDao.getInterestCount(1)

        // Then
        assertThat(stored).isEqualTo(3)

    }
}
