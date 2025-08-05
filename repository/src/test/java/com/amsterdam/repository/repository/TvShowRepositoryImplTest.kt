package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CategoryRepository
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.TvShowLocalSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.mapper.remote.toEntityList
import com.amsterdam.repository.utils.remoteTvShowItemDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class TvShowRepositoryImplTest {
    private lateinit var tvShowRepository: TvShowRepositoryImpl
    private val categoryRepository: CategoryRepository = mockk()
    private val localTvDataSource: TvShowLocalSource = mockk()
    private val remoteTvDataSource: TvShowsRemoteSource = mockk()
    private val preferences: AppPreferences = mockk()

    @BeforeEach
    fun setUp() {
        tvShowRepository = TvShowRepositoryImpl(
            categoryRepository,
            localTvDataSource,
            remoteTvDataSource,
            preferences
        )
    }


    @Test
    fun `getTvShowByKeyword should return list of TvShow`() = runTest {
        val keyword = "Breaking"
        val page = 1
        val tvShowsPerPage = 20
        val expectedTvShows = listOf(
            remoteTvShowItemDto
        )
        coEvery {
            remoteTvDataSource.getTvShowsByKeyword(
                keyword,
                page
            )
        } returns RemoteTvShowResponse(
            page = 1,
            results = expectedTvShows,
            totalPages = 1,
            totalResults = 1
        )

        val result = tvShowRepository.getTvShowByKeyword(keyword, page, tvShowsPerPage)
        assertThat(result).isEqualTo(expectedTvShows.toEntityList())
        coVerify { remoteTvDataSource.getTvShowsByKeyword(keyword, page) }


    }

}