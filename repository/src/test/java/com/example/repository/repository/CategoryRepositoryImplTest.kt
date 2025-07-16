package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category
import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.mapper.local.CategoryLocalMapper
import com.example.repository.mapper.remote.CategoryRemoteMapper
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryRepositoryImplTest {

    private lateinit var repository: CategoryRepository

    private val remoteDataSource: CategoryRemoteSource = mockk()
    private val localDataSource: CategoryLocalSource = mockk()
    private val localMapper: CategoryLocalMapper = mockk()
    private val remoteMapper: CategoryRemoteMapper = mockk()

    @BeforeEach
    fun setup() {
        repository = CategoryRepositoryImpl(
            categoryRemoteSource = remoteDataSource,
            categoryLocalSource = localDataSource,
            categoryLocalMapper = localMapper,
            categoryRemoteMapper = remoteMapper
        )
    }

    @Test
    fun `should return mapped local movie categories when local is not empty`() = runTest {
        val local = listOf(LocalMovieCategoryDto(1, "Action"))
        val mapped = listOf(
            Category(
                MovieGenre.ACTION.ordinal.toLong(),
                MovieGenre.ACTION.name, ""
            )
        )

        coEvery { localDataSource.getMovieCategories() } returns local
        every { localMapper.mapToMovieCategories(local) } returns listOf(MovieGenre.ACTION)

        val result = repository.getMovieCategories()

        assertThat(result).isEqualTo(mapped)
        coVerify(exactly = 0) { remoteDataSource.getMovieCategories() }
    }

    @Test
    fun `should return mapped local tv show categories when local is not empty`() = runTest {
        val local = listOf(LocalTvShowCategoryDto(3, "Drama"))
        val mapped = listOf(
            Category(
                TvShowGenre.DRAMA.ordinal.toLong(),
                TvShowGenre.DRAMA.name,
                ""
            )
        )

        coEvery { localDataSource.getTvShowCategories() } returns local
        every { localMapper.mapToTvShowCategories(local) } returns listOf(TvShowGenre.DRAMA)

        val result = repository.getTvShowCategories()

        assertThat(result).isEqualTo(mapped)
        coVerify(exactly = 0) { remoteDataSource.getTvShowCategories() }
    }
}
