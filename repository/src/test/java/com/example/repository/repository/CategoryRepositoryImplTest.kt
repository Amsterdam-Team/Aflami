package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category
import com.example.repository.datasource.local.LocalCategoryDataSource
import com.example.repository.datasource.remote.RemoteCategoryDatasource
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.remote.RemoteCategoryDto
import com.example.repository.dto.remote.RemoteCategoryResponse
import com.example.repository.mapper.local.CategoryLocalMapper
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryRepositoryImplTest {

    private lateinit var repository: CategoryRepository

    private val remoteDataSource: RemoteCategoryDatasource = mockk()
    private val localDataSource: LocalCategoryDataSource = mockk()
    private val mapper: CategoryLocalMapper = mockk()

    @BeforeEach
    fun setup() {
        repository = CategoryRepositoryImpl(
            remoteCategoryDatasource = remoteDataSource,
            localCategoryDatasource = localDataSource,
            categoryLocalMapper = mapper
        )
    }

    @Test
    fun `should return mapped local movie categories when local is not empty`() = runTest {
        val local = listOf(LocalMovieCategoryDto(1, "Action"))
        val mapped = listOf(Category(1, "Action", ""))

        coEvery { localDataSource.getAllMovieCategories() } returns local
        every { mapper.mapListFromMovieLocal(local) } returns mapped

        val result = repository.getMovieCategories()

        assertThat(result).isEqualTo(mapped)
        coVerify(exactly = 0) { remoteDataSource.getMovieCategories() }
    }

    @Test
    fun `should fetch remote movie categories and cache locally when local is empty`() = runTest {
        val remote = RemoteCategoryResponse(listOf(RemoteCategoryDto(2, "Comedy")))
        val localMapped = listOf(LocalMovieCategoryDto(2, "Comedy"))
        val finalMapped = listOf(Category(2, "Comedy", ""))

        coEvery { localDataSource.getAllMovieCategories() } returns emptyList()
        coEvery { remoteDataSource.getMovieCategories() } returns remote
        every { mapper.mapToLocalMovieCategories(remote) } returns localMapped
        coEvery { localDataSource.upsertAllMovieCategories(localMapped) } just Runs
        every { mapper.mapListFromMovieLocal(localMapped) } returns finalMapped

        val result = repository.getMovieCategories()

        assertThat(result).isEqualTo(finalMapped)
        coVerify { remoteDataSource.getMovieCategories() }
        coVerify { localDataSource.upsertAllMovieCategories(localMapped) }
    }

    @Test
    fun `should return mapped local tv show categories when local is not empty`() = runTest {
        val local = listOf(LocalTvShowCategoryDto(3, "Drama"))
        val mapped = listOf(Category(3, "Drama", ""))

        coEvery { localDataSource.getAllTvShowCategories() } returns local
        every { mapper.mapListFromTvShowLocal(local) } returns mapped

        val result = repository.getTvShowCategories()

        assertThat(result).isEqualTo(mapped)
        coVerify(exactly = 0) { remoteDataSource.getTvShowCategories() }
    }

    @Test
    fun `should fetch remote tv show categories and cache locally when local is empty`() = runTest {
        val remote = RemoteCategoryResponse(listOf(RemoteCategoryDto(4, "Fantasy")))
        val localMapped = listOf(LocalTvShowCategoryDto(4, "Fantasy"))
        val finalMapped = listOf(Category(4, "Fantasy", ""))

        coEvery { localDataSource.getAllTvShowCategories() } returns emptyList()
        coEvery { remoteDataSource.getTvShowCategories() } returns remote
        every { mapper.mapToLocalTvShowCategories(remote) } returns localMapped
        coEvery { localDataSource.upsertAllTvShowCategories(localMapped) } just Runs
        every { mapper.mapListFromTvShowLocal(localMapped) } returns finalMapped

        val result = repository.getTvShowCategories()

        assertThat(result).isEqualTo(finalMapped)
        coVerify { remoteDataSource.getTvShowCategories() }
        coVerify { localDataSource.upsertAllTvShowCategories(localMapped) }
    }
}
