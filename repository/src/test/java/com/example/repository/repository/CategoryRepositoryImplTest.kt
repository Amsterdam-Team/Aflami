/*
package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category
import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.mapper.local.MovieCategoryLocalMapper
import com.example.repository.mapper.local.TvShowCategoryLocalMapper
import com.example.repository.mapper.remote.CategoryRemoteMapper
import com.example.repository.mapper.remoteToLocal.MovieCategoryRemoteLocalMapper
import com.example.repository.mapper.remoteToLocal.TvShowCategoryRemoteLocalMapper
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
    private val movieCategoryLocalMapper: MovieCategoryLocalMapper = mockk()
    private val categoryRemoteMapper: CategoryRemoteMapper = mockk()
    private val movieCategoryRemoteLocalMapper: MovieCategoryRemoteLocalMapper = mockk()
    private val tvShowCategoryRemoteLocalMapper: TvShowCategoryRemoteLocalMapper = mockk()
    private val tvShowCategoryLocalMapper: TvShowCategoryLocalMapper = mockk()

    @BeforeEach
    fun setup() {
        repository = CategoryRepositoryImpl(
            categoryRemoteSource = remoteDataSource,
            categoryLocalSource = localDataSource,
            movieCategoryLocalMapper = movieCategoryLocalMapper,
            categoryRemoteMapper = categoryRemoteMapper,
            movieCategoryRemoteLocalMapper = movieCategoryRemoteLocalMapper,
            tvShowCategoryRemoteLocalMapper = tvShowCategoryRemoteLocalMapper,
            tvShowCategoryLocalMapper = tvShowCategoryLocalMapper
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
        every { movieCategoryLocalMapper.toEntityList(local) } returns emptyList()

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
        every { movieCategoryLocalMapper.toEntityList(
            dtoList = emptyList()
        ) } returns emptyList()

        val result = repository.getTvShowCategories()

        assertThat(result).isEqualTo(mapped)
        coVerify(exactly = 0) { remoteDataSource.getTvShowCategories() }
    }
}
*/
