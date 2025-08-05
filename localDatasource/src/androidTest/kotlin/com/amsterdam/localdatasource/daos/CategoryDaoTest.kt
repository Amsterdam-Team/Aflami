package com.amsterdam.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.daos.CategoryDao
import com.amsterdam.localdatasource.utils.createLocalMovieCategoryDto
import com.amsterdam.localdatasource.utils.createLocalTvShowCategoryDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CategoryDaoTest {

    private lateinit var aflamiDatabase: AflamiDatabase
    private lateinit var categoryDao: CategoryDao
    val storedLanguage = "en"

    @BeforeEach
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        aflamiDatabase = Room.inMemoryDatabaseBuilder(appContext, AflamiDatabase::class.java).build()
        categoryDao = aflamiDatabase.categoryDao()
    }

    @AfterEach
    fun tearDown() {
        aflamiDatabase.close()
    }

    @Test
    fun upsertAllMovieCategories_shouldInsertMovieCategories_whenCalled() = runTest {
        // Given
        val categories = listOf(
            createLocalMovieCategoryDto(categoryId = 1, name = "A"),
            createLocalMovieCategoryDto(categoryId = 2, name = "B")
        )

        // When
        categoryDao.upsertAllMovieCategories(categories)

        // Then
        val stored = categoryDao.getAllMovieCategories(storedLanguage)
        assertThat(stored).isEqualTo(categories)
    }

    @Test
    fun upsertAllMovieCategories_shouldUpdateMovieCategories_whenAlreadyStored() = runTest {
        // Given
        val initial = listOf(
            createLocalMovieCategoryDto(categoryId = 1, name = "Whenion", storedLanguage = "en"),
            createLocalMovieCategoryDto(categoryId = 2, name = "Drama", storedLanguage = "en")
        )
        categoryDao.upsertAllMovieCategories(initial)

        val updated = listOf(
            createLocalMovieCategoryDto(categoryId = 1, name = "Adventure", storedLanguage = "en"),
            createLocalMovieCategoryDto(categoryId = 2, name = "Comedy", storedLanguage = "en")
        )

        // When
        categoryDao.upsertAllMovieCategories(updated)

        // Then
        val stored = categoryDao.getAllMovieCategories(storedLanguage)
        assertThat(stored).isEqualTo(updated)
    }

    @Test
    fun getAllMovieCategories_shouldReturnEmptyList_whenNoDataInserted() = runTest {
        // When
        val result = categoryDao.getAllMovieCategories(storedLanguage)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun upsertAllMovieCategories_shouldReplaceDuplicateCategory_whenSamePrimaryKeyExists() = runTest {
        // Given
        val original = listOf(
            createLocalMovieCategoryDto(categoryId = 5, name = "Sci-Fi", storedLanguage = "en")
        )
        categoryDao.upsertAllMovieCategories(original)

        val updated = listOf(
            createLocalMovieCategoryDto(categoryId = 5, name = "Fantasy", storedLanguage = "en")
        )

        // When
        categoryDao.upsertAllMovieCategories(updated)

        // Then
        val stored = categoryDao.getAllMovieCategories(storedLanguage)
        assertThat(stored).containsExactly(updated.first())
    }

    @Test
    fun upsertAllMovieCategories_shouldInsertOnlyOne_whenDuplicateNameExists() = runTest {
        // Given
        val categories = listOf(
            createLocalMovieCategoryDto(categoryId = 1, name = "Old", storedLanguage = "en"),
            createLocalMovieCategoryDto(categoryId = 2, name = "Old", storedLanguage = "en")
        )

        // When
        categoryDao.upsertAllMovieCategories(categories)

        // Then
        val stored = categoryDao.getAllMovieCategories(storedLanguage)
        assertThat(stored).containsExactly(categories.first())
    }

    @Test
    fun upsertAllTvShowCategories_shouldInsertTvShowCategories_whenCalled() = runTest {
        // Given
        val categories = listOf(
            createLocalTvShowCategoryDto(categoryId = 1L, name = "Popular"),
            createLocalTvShowCategoryDto(categoryId = 2L, name = "Trending")
        )

        // When
        categoryDao.upsertAllTvShowCategories(categories)

        // Then
        val stored = categoryDao.getAllTvShowCategories(storedLanguage)
        assertThat(stored).isEqualTo(categories)
    }

    @Test
    fun upsertAllTvShowCategories_shouldInsertOnlyOne_whenDuplicateNameExists() = runTest {
        // Given
        val categories = listOf(
            createLocalTvShowCategoryDto(categoryId = 1, name = "Old", storedLanguage = "en"),
            createLocalTvShowCategoryDto(categoryId = 2, name = "Old", storedLanguage = "en")
        )

        // When
        categoryDao.upsertAllTvShowCategories(categories)

        // Then
        val stored = categoryDao.getAllTvShowCategories(storedLanguage)
        assertThat(stored).containsExactly(categories.first())
    }

    @Test
    fun getAllTvShowCategories_shouldReturnEmptyList_whenNoDataInserted() = runTest {
        // When
        val result = categoryDao.getAllTvShowCategories(storedLanguage)

        // Assert
        assertThat(result).isEmpty()
    }

    @Test
    fun upsertAllTvShowCategories_shouldReplaceDuplicateCategory_whenSamePrimaryKeyExists() = runTest {
        // Given
        val original = listOf(
            createLocalTvShowCategoryDto(categoryId = 10, name = "Original", storedLanguage = "en")
        )
        categoryDao.upsertAllTvShowCategories(original)

        val updated = listOf(
            createLocalTvShowCategoryDto(categoryId = 10, name = "Updated", storedLanguage = "en")
        )

        // When
        categoryDao.upsertAllTvShowCategories(updated)

        // Then
        val stored = categoryDao.getAllTvShowCategories(storedLanguage)
        assertThat(stored).containsExactly(updated.first())
    }
}
