package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.CategoryDao
import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class CategoryLocalDataSourceImplTest {
    private val dao by lazy { mockk<CategoryDao>(relaxed = true) }
    private val categoryLocalDataSourceImpl by lazy { CategoryLocalDataSourceImpl(dao) }

    @Test
    fun `upsertMovieCategories should call dao with correct data when called`() = runTest {
        categoryLocalDataSourceImpl.upsertMovieCategories(movieCategories)

        coVerify(exactly = 1) { dao.upsertAllMovieCategories(movieCategories) }
    }

    @Test
    fun `upsertTvShowCategories should call dao with correct data when called`() = runTest {
        categoryLocalDataSourceImpl.upsertTvShowCategories(tvShowCategories)

        coVerify(exactly = 1) { dao.upsertAllTvShowCategories(tvShowCategories) }
    }

    @Test
    fun `getMovieCategories should return data from dao when there is data returned`() = runTest {
        coEvery { dao.getAllMovieCategories() } returns movieCategories

        val result = categoryLocalDataSourceImpl.getMovieCategories()

        assertThat(result).isEqualTo(movieCategories)
    }

    @Test
    fun `getMovieCategories should return emptyList from dao when there is no data`() = runTest {
        coEvery { dao.getAllMovieCategories() } returns emptyList()

        val result = categoryLocalDataSourceImpl.getMovieCategories()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowCategories should return data from dao when there is data`() = runTest {
        coEvery { dao.getAllTvShowCategories() } returns tvShowCategories

        val result = categoryLocalDataSourceImpl.getTvShowCategories()

        assertThat(result).isEqualTo(tvShowCategories)
    }

    @Test
    fun `getTvShowCategories should return emptyList from dao when there is no data`() = runTest {
        coEvery { dao.getAllTvShowCategories() } returns emptyList()

        val result = categoryLocalDataSourceImpl.getTvShowCategories()

        assertThat(result).isEmpty()
    }
}

private val movieCategories = listOf(MovieCategoryLocalDto(1))
private val tvShowCategories = listOf(TvShowCategoryLocalDto(1))
