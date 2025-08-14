package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.CategoryDao
import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CategoryLocalDataSourceImplTest {

    private lateinit var dao: CategoryDao
    private lateinit var categoryLocalDataSourceImpl: CategoryLocalDataDataSourceImpl

    @BeforeEach
    fun setup() {
        dao = mockk(relaxed = true)
        categoryLocalDataSourceImpl = CategoryLocalDataDataSourceImpl(dao)
    }

    @Test
    fun `upsertMovieCategories should call dao with correct data`() = runTest {
        val categories = listOf(MovieCategoryLocalDto(1))

        categoryLocalDataSourceImpl.upsertMovieCategories(categories)

        coVerify (exactly = 1) { dao.upsertAllMovieCategories(categories) }
    }

    @Test
    fun `upsertTvShowCategories should call dao with correct data`() = runTest {
        val categories = listOf(TvShowCategoryLocalDto(1))

        categoryLocalDataSourceImpl.upsertTvShowCategories(categories)

        coVerify (exactly = 1) { dao.upsertAllTvShowCategories(categories) }
    }

    @Test
    fun `getMovieCategories should return data from dao`() = runTest {
        val expected = listOf(MovieCategoryLocalDto(1))
        coEvery { dao.getAllMovieCategories() } returns expected

        val result = categoryLocalDataSourceImpl.getMovieCategories()

        assertThat(result).isEqualTo(expected)
    }
    @Test
    fun `getMovieCategories should return emptyList from dao`() = runTest {
        //Given
        val expected = emptyList<MovieCategoryLocalDto>()
        //When
        coEvery { dao.getAllMovieCategories() } returns expected
        val result = categoryLocalDataSourceImpl.getMovieCategories()
        //Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowCategories should return data from dao`() = runTest {
        //Given
        val expected = listOf(TvShowCategoryLocalDto(1))
        //When
        coEvery { dao.getAllTvShowCategories() } returns expected
        val result = categoryLocalDataSourceImpl.getTvShowCategories()
        //Then
        assertThat(result).isEqualTo(expected)
    }
}
