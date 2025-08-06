package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CategoryRepository
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.CategoryLocalSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteSource
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import com.amsterdam.repository.mapper.local.toEntityList
import com.amsterdam.repository.mapper.remote.toEntityList
import com.amsterdam.repository.mapper.remoteToLocal.toLocalDtoList
import com.amsterdam.repository.mapper.remoteToLocal.toLocalTvShowCategoryDtoList
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CategoryRepositoryImplTest {

    private lateinit var repository: CategoryRepository

    private val remoteDataSource: CategoryRemoteSource = mockk()
    private val localDataSource: CategoryLocalSource = mockk()
    private val preferences: AppPreferences = mockk()

    private val testLanguage = "en"
    private val testMovieCategoryDto =
        LocalMovieCategoryDto(categoryId = 1, storedLanguage = testLanguage, name = "Action")
    private val testTvShowCategoryDto =
        LocalTvShowCategoryDto(categoryId = 3, storedLanguage = testLanguage, name = "Drama")
    private val testRemoteCategoryDto = RemoteCategoryDto(id = 1, name = "Action")

    @BeforeEach
    fun setup() {
        clearAllMocks()
        repository = CategoryRepositoryImpl(
            categoryRemoteSource = remoteDataSource,
            categoryLocalSource = localDataSource,
            preferences = preferences
        )

        coEvery { preferences.getAppLanguage() } returns flowOf(testLanguage)
    }

    @Test
    fun `getMovieCategories should return local data when available`() = runTest {
        // Arrange
        val localCategories = listOf(testMovieCategoryDto)
        val expectedCategories =
            localCategories.toEntityList()

        coEvery { localDataSource.getMovieCategories(testLanguage) } returns localCategories

        // Act
        val result = repository.getMovieCategories()

        // Assert
        assertThat(result).isEqualTo(expectedCategories)
        coVerify(exactly = 1) { localDataSource.getMovieCategories(testLanguage) }
        coVerify(exactly = 0) { remoteDataSource.getMovieCategories() }
        coVerify(exactly = 0) { localDataSource.upsertMovieCategories(any()) }
    }

    @Test
    fun `getTvShowCategories should return local data when available`() = runTest {
        // Arrange
        val localTvShowCategories = listOf(testTvShowCategoryDto)
        val expectedTvShowCategories = localTvShowCategories.toEntityList()

        coEvery { localDataSource.getTvShowCategories(testLanguage) } returns localTvShowCategories

        // Act
        val result = repository.getTvShowCategories()

        // Assert
        assertThat(result).isEqualTo(expectedTvShowCategories)
        coVerify(exactly = 1) { localDataSource.getTvShowCategories(testLanguage) }
        coVerify(exactly = 0) { remoteDataSource.getTvShowCategories() }
        coVerify(exactly = 0) { localDataSource.upsertTvShowCategories(any()) }
    }

    @Test
    fun `getMovieCategories should fetch from remote and cache if local is empty`() = runTest {
        // Arrange
        val remoteResponse = RemoteCategoryResponse(genres = listOf(testRemoteCategoryDto))
        val expectedEntityList = remoteResponse.genres.toEntityList()

        coEvery { localDataSource.getMovieCategories(testLanguage) } returns emptyList()
        coEvery { remoteDataSource.getMovieCategories() } returns remoteResponse
        coJustRun { localDataSource.upsertMovieCategories(any()) }

        // Act
        val result = repository.getMovieCategories()

        // Assert
        assertThat(result).isEqualTo(expectedEntityList)
        coVerify(exactly = 1) { localDataSource.getMovieCategories(testLanguage) }
        coVerify(exactly = 1) { remoteDataSource.getMovieCategories() }
        coVerify(exactly = 1) {
            localDataSource.upsertMovieCategories(
                remoteResponse.genres.toLocalDtoList(
                    testLanguage
                )
            )
        }
    }

    @Test
    fun `getTvShowCategories should fetch from remote and cache if local is empty`() = runTest {
        // Arrange
        val remoteResponse =
            RemoteCategoryResponse(genres = listOf(RemoteCategoryDto(id = 3, name = "Drama")))
        val expectedEntityList = remoteResponse.genres.toEntityList()

        coEvery { localDataSource.getTvShowCategories(testLanguage) } returns emptyList()
        coEvery { remoteDataSource.getTvShowCategories() } returns remoteResponse
        coJustRun { localDataSource.upsertTvShowCategories(any()) }

        // Act
        val result = repository.getTvShowCategories()

        // Assert
        assertThat(result).isEqualTo(expectedEntityList)
        coVerify(exactly = 1) { localDataSource.getTvShowCategories(testLanguage) }
        coVerify(exactly = 1) { remoteDataSource.getTvShowCategories() }
        coVerify(exactly = 1) {
            localDataSource.upsertTvShowCategories(
                remoteResponse.genres.toLocalTvShowCategoryDtoList(
                    testLanguage
                )
            )
        }
    }

    @Test
    fun `getMovieCategories should throw exception if remote fetch fails`() = runTest {
        // Arrange
        val expectedException = RuntimeException("Remote server down!")
        coEvery { localDataSource.getMovieCategories(testLanguage) } returns emptyList()
        coEvery { remoteDataSource.getMovieCategories() } throws expectedException

        // Act & Assert
        val thrownException = assertThrows<RuntimeException> {
            repository.getMovieCategories()
        }

        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { localDataSource.getMovieCategories(testLanguage) }
        coVerify(exactly = 1) { remoteDataSource.getMovieCategories() }
        coVerify(exactly = 0) { localDataSource.upsertMovieCategories(any()) }
    }

    @Test
    fun `getTvShowCategories should throw exception if remote fetch fails`() = runTest {
        // Arrange
        val expectedException = RuntimeException("Remote server down!")
        coEvery { localDataSource.getTvShowCategories(testLanguage) } returns emptyList()
        coEvery { remoteDataSource.getTvShowCategories() } throws expectedException

        // Act & Assert
        val thrownException = assertThrows<RuntimeException> {
            repository.getTvShowCategories()
        }

        assertThat(thrownException).isEqualTo(expectedException)
        coVerify(exactly = 1) { localDataSource.getTvShowCategories(testLanguage) }
        coVerify(exactly = 1) { remoteDataSource.getTvShowCategories() }
        coVerify(exactly = 0) { localDataSource.upsertTvShowCategories(any()) }
    }
}