package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CategoryRepository
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.MovieLocalSource
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.dto.remote.RemoteMovieResponse
import com.amsterdam.repository.mapper.remote.toMovieEntityList
import com.amsterdam.repository.utils.remoteMovieItemDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class MovieRepositoryImplTest {
    private lateinit var movieRepository: MovieRepositoryImpl
    private val categoryRepository: CategoryRepository = mockk()
    private val localMovieDataSource: MovieLocalSource = mockk()
    private val remoteMovieDataSource: MovieRemoteSource = mockk()
    private val preferences: AppPreferences = mockk()

    @BeforeEach
    fun setUp() {
        movieRepository = MovieRepositoryImpl(
            categoryRepository,
            localMovieDataSource,
            remoteMovieDataSource,
            preferences
        )
    }

    @Test
    fun `getMoviesByKeyword should return list of movies`() = runTest {
        val keyword = "keyword"
        val page = 1
        val moviesPerPage = 20
        val expectedMovies = listOf(
            remoteMovieItemDto
        )
        coEvery {
            remoteMovieDataSource.getMoviesByKeyword(
                keyword,
                page
            )
        } returns RemoteMovieResponse(
            page = 1,
            results = expectedMovies,
            totalPages = 1,
            totalResults = 1
        )
        val result = movieRepository.getMoviesByKeyword(keyword, page, moviesPerPage)
        assertThat(result).isEqualTo(expectedMovies.toMovieEntityList())
        coVerify { remoteMovieDataSource.getMoviesByKeyword(keyword, page) }


    }


}