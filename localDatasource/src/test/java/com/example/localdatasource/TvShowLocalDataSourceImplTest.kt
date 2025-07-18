package com.example.localdatasource

import com.example.localdatasource.roomDataBase.daos.TvShowDao
import com.example.localdatasource.roomDataBase.datasource.TvShowLocalDataSourceImpl
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategory
import com.example.repository.dto.local.utils.SearchType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class TvShowLocalDataSourceImplTest {
    private lateinit var dao: TvShowDao
    private lateinit var tvShowLocalDataSourceImpl: TvShowLocalDataSourceImpl


    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        tvShowLocalDataSourceImpl = TvShowLocalDataSourceImpl(dao)
    }

    @Test
    fun `getTvShowsByKeywordAndSearchType should call dao with correct data`() =runTest{
        //Given
        val searchKeyword = "action"
        val searchType = SearchType.BY_KEYWORD

        coEvery { dao.getTvShowsBySearchKeyword(searchKeyword, searchType) } returns expectedTvShowWithCategory
        //When
       val result= tvShowLocalDataSourceImpl.getTvShowsByKeywordAndSearchType(searchKeyword, searchType)
        //Then
        coVerify { dao.getTvShowsBySearchKeyword(searchKeyword, searchType) }
        assertEquals(expectedTvShowWithCategory, result)

    }
    @Test
    fun `addTvShows should call dao with correct data`() =runTest {
        //Given
        val searchKeyword = "action"
        //When
        tvShowLocalDataSourceImpl.addTvShows(tvShows, searchKeyword)
        //Then
        coVerify { dao.addAllTvShows(tvShows) }
        coVerify { dao.insertTvShowSearchMappings(any()) }
    }

}