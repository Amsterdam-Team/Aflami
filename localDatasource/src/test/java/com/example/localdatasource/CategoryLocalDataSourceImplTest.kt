package com.example.localdatasource


import com.example.localdatasource.roomDataBase.daos.CategoryDao
import com.example.localdatasource.roomDataBase.datasource.CategoryLocalDataSourceImpl
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CategoryLocalDataSourceImplTest {

    private lateinit var dao: CategoryDao
    private lateinit var categoryLocalDataSourceImpl: CategoryLocalDataSourceImpl

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        categoryLocalDataSourceImpl = CategoryLocalDataSourceImpl(dao)
    }

    @Test
    fun `upsertMovieCategories should call dao with correct data`() = runTest {
        val categories = listOf(LocalMovieCategoryDto(1, "Action"))

        categoryLocalDataSourceImpl.upsertMovieCategories(categories)

        coVerify { dao.upsertAllMovieCategories(categories) }
    }

    @Test
    fun `upsertTvShowCategories should call dao with correct data`() = runTest {
        val categories = listOf(LocalTvShowCategoryDto(1, "Drama"))

        categoryLocalDataSourceImpl.upsertTvShowCategories(categories)

        coVerify { dao.upsertAllTvShowCategories(categories) }
    }

    @Test
    fun `getMovieCategories should return data from dao`() = runTest {
        val expected = listOf(LocalMovieCategoryDto(1, "Comedy"))
        coEvery { dao.getAllMovieCategories() } returns expected

        val result = categoryLocalDataSourceImpl.getMovieCategories()

        assertEquals(expected, result)
    }
    @Test
    fun `getMovieCategories should return emptyList from dao`() = runTest {
        //Given
        val expected = emptyList<LocalMovieCategoryDto>()
        //When
        coEvery { dao.getAllMovieCategories() } returns expected
        val result = categoryLocalDataSourceImpl.getMovieCategories()
        //Then
        assertEquals(expected, result)

    }

    @Test
    fun `getTvShowCategories should return data from dao`() = runTest {
        //Given
        val expected = listOf(LocalTvShowCategoryDto(1, "All"))
        //When
        coEvery { dao.getAllTvShowCategories() } returns expected
        val result = categoryLocalDataSourceImpl.getTvShowCategories()
        //Then
        assertEquals(expected, result)
    }
}
