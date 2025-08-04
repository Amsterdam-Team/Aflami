package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CategoryRepository
import com.amsterdam.entity.Category
import com.amsterdam.repository.datasource.local.CategoryLocalSource
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import com.amsterdam.repository.mapper.remote.CategoryRemoteMapper
import com.amsterdam.repository.mapper.remoteToLocal.MovieCategoryRemoteLocalMapper
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class CategoryRepositoryImplTest {

    private lateinit var repository: CategoryRepository

    private val remoteDataSource: CategoryRemoteSource = mockk()
    private val localDataSource: CategoryLocalSource = mockk()
    private val movieCategoryLocalMapper: MovieCategoryLocalMapper = mockk()
    private val categoryRemoteMapper: CategoryRemoteMapper = mockk()
    private val movieCategoryRemoteLocalMapper: MovieCategoryRemoteLocalMapper = mockk()
    private val tvShowCategoryRemoteLocalMapper: TvShowCategoryRemoteLocalMapper = mockk()
    private val tvShowCategoryLocalMapper: TvShowCategoryLocalMapper = mockk()

    private val testLanguage = "en"

    @BeforeEach
    fun setup() {
        clearAllMocks()

        repository = CategoryRepositoryImpl(
            categoryRemoteSource = remoteDataSource,
            categoryLocalSource = localDataSource,
            movieCategoryLocalMapper = movieCategoryLocalMapper,
            categoryRemoteMapper = categoryRemoteMapper,
            movieCategoryRemoteLocalMapper = movieCategoryRemoteLocalMapper,
            tvShowCategoryRemoteLocalMapper = tvShowCategoryRemoteLocalMapper,
            tvShowCategoryLocalMapper = tvShowCategoryLocalMapper
        )
        every { movieCategoryLocalMapper.toEntityList(emptyList()) } returns emptyList()
        every { tvShowCategoryLocalMapper.toEntityList(emptyList()) } returns emptyList()
    }

    @Test
    fun `getMovieCategories should return mapped local movie categories when local is not empty`() =
        runTest {
            val localCategoryDto = LocalMovieCategoryDto(
                categoryId = 1,
                storedLanguage = testLanguage,
                name = "Action"
            )
            val localCategories = listOf(localCategoryDto)
            val mappedCategory = Category(id = 1, name = "Action", imageUrl = "")
            val mappedCategories = listOf(mappedCategory)

            coEvery { localDataSource.getMovieCategories() } returns localCategories
            every { movieCategoryLocalMapper.toEntityList(localCategories) } returns mappedCategories

            val result = repository.getMovieCategories()

            assertThat(result).isEqualTo(mappedCategories)
            coVerify(exactly = 0) { remoteDataSource.getMovieCategories() }
            coVerify(exactly = 1) { localDataSource.getMovieCategories() }
            verify(exactly = 1) { movieCategoryLocalMapper.toEntityList(localCategories) }
        }

    @Test
    fun `getTvShowCategories should return mapped local tv show categories when local is not empty`() =
        runTest {
            val localTvShowCategoryDto = LocalTvShowCategoryDto(
                categoryId = 3,
                storedLanguage = testLanguage,
                name = "Drama"
            )
            val localTvShowCategories = listOf(localTvShowCategoryDto)
            val mappedTvShowCategory = Category(id = 3, name = "Drama", imageUrl = "")
            val mappedTvShowCategories = listOf(mappedTvShowCategory)

            coEvery { localDataSource.getTvShowCategories() } returns localTvShowCategories
            every { tvShowCategoryLocalMapper.toEntityList(localTvShowCategories) } returns mappedTvShowCategories

            val result = repository.getTvShowCategories()

            assertThat(result).isEqualTo(mappedTvShowCategories)
            coVerify(exactly = 0) { remoteDataSource.getTvShowCategories() }
            coVerify(exactly = 1) { localDataSource.getTvShowCategories() }
            verify(exactly = 1) { tvShowCategoryLocalMapper.toEntityList(localTvShowCategories) }
        }

    @Test
    fun `getMovieCategories should fetch from remote, save locally, and return mapped categories if local is empty`() =
        runTest {
            val remoteCategoryDto = RemoteCategoryDto(id = 1, name = "Action")
            val remoteResponse = RemoteCategoryResponse(genres = listOf(remoteCategoryDto))
            val localSavedDto = LocalMovieCategoryDto(
                categoryId = 1,
                storedLanguage = testLanguage,
                name = "Action"
            )
            val mappedDomainCategory = Category(id = 1, name = "Action", imageUrl = "")

            coEvery { localDataSource.getMovieCategories() } returns emptyList()

            coEvery { remoteDataSource.getMovieCategories() } returns remoteResponse

            every {
                movieCategoryRemoteLocalMapper.toLocalList(
                    remoteResponse.genres,
                    listOf(testLanguage)
                )
            } returns listOf(localSavedDto)

            coJustRun { localDataSource.upsertMovieCategories(listOf(localSavedDto)) }

            every { categoryRemoteMapper.toEntityList(remoteResponse.genres) } returns listOf(
                mappedDomainCategory
            )

            val result = repository.getMovieCategories()

            assertThat(result).isEqualTo(listOf(mappedDomainCategory))

            coVerify(exactly = 1) { localDataSource.getMovieCategories() }
            coVerify(exactly = 1) { remoteDataSource.getMovieCategories() }
            verify(exactly = 1) {
                movieCategoryRemoteLocalMapper.toLocalList(
                    remoteResponse.genres,
                    listOf(testLanguage)
                )
            }
            coVerify(exactly = 1) { localDataSource.upsertMovieCategories(listOf(localSavedDto)) }
            verify(exactly = 1) { categoryRemoteMapper.toEntityList(remoteResponse.genres) }
        }

    @Test
    fun `getTvShowCategories should fetch from remote, save locally, and return mapped categories if local is empty`() =
        runTest {
            val remoteCategoryDto = RemoteCategoryDto(id = 3, name = "Drama")
            val remoteResponse = RemoteCategoryResponse(genres = listOf(remoteCategoryDto))
            val localSavedDto = LocalTvShowCategoryDto(
                categoryId = 3,
                storedLanguage = testLanguage,
                name = "Drama"
            )
            val mappedDomainCategory = Category(id = 3, name = "Drama", imageUrl = "")

            coEvery { localDataSource.getTvShowCategories() } returns emptyList()

            coEvery { remoteDataSource.getTvShowCategories() } returns remoteResponse

            every {
                tvShowCategoryRemoteLocalMapper.toLocalList(
                    remoteResponse.genres,
                    listOf(testLanguage)
                )
            } returns listOf(localSavedDto)

            coJustRun { localDataSource.upsertTvShowCategories(listOf(localSavedDto)) }

            every { categoryRemoteMapper.toEntityList(remoteResponse.genres) } returns listOf(
                mappedDomainCategory
            )

            val result = repository.getTvShowCategories()

            assertThat(result).isEqualTo(listOf(mappedDomainCategory))


            coVerify(exactly = 1) { localDataSource.getTvShowCategories() }
            coVerify(exactly = 1) { remoteDataSource.getTvShowCategories() }
            verify(exactly = 1) {
                tvShowCategoryRemoteLocalMapper.toLocalList(
                    remoteResponse.genres,
                    listOf(testLanguage)
                )
            }
            coVerify(exactly = 1) { localDataSource.upsertTvShowCategories(listOf(localSavedDto)) }
            verify(exactly = 1) { categoryRemoteMapper.toEntityList(remoteResponse.genres) }
        }

    @Test
    fun `getMovieCategories should throw exception if local source fails`() = runTest {
        val expectedException = RuntimeException("Database corruption!")

        coEvery { localDataSource.getMovieCategories() } throws expectedException

        val thrownException = assertThrows<RuntimeException> {
            repository.getMovieCategories()
        }

        assertThat(thrownException).isEqualTo(expectedException)

        coVerify(exactly = 1) { localDataSource.getMovieCategories() }
        coVerify(exactly = 0) { remoteDataSource.getMovieCategories() }
        verify(exactly = 0) { movieCategoryRemoteLocalMapper.toLocalList(any(), any()) }
        coVerify(exactly = 0) { localDataSource.upsertMovieCategories(any()) }
        verify(exactly = 0) { categoryRemoteMapper.toEntityList(any()) }
    }

    @Test
    fun `getTvShowCategories should throw exception if local source fails`() = runTest {
        val expectedException = RuntimeException("Database read error!")

        coEvery { localDataSource.getTvShowCategories() } throws expectedException

        val thrownException = assertThrows<RuntimeException> {
            repository.getTvShowCategories()
        }

        assertThat(thrownException).isEqualTo(expectedException)

        coVerify(exactly = 1) { localDataSource.getTvShowCategories() }
        coVerify(exactly = 0) { remoteDataSource.getTvShowCategories() }
        verify(exactly = 0) { tvShowCategoryRemoteLocalMapper.toLocalList(any(), any()) }
        coVerify(exactly = 0) { localDataSource.upsertTvShowCategories(any()) }
        verify(exactly = 0) { categoryRemoteMapper.toEntityList(any()) }
    }
}