package com.example.repository.repository

import com.example.domain.repository.TvShowRepository
import com.example.entity.TvShow
import com.example.repository.datasource.local.LocalRecentSearchDataSource
import com.example.repository.datasource.local.LocalTvShowDataSource
import com.example.repository.datasource.remote.RemoteTvShowsDatasource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.mapper.local.TvShowLocalMapper
import com.example.repository.mapper.remote.RemoteTvShowMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class TvShowRepositoryImpl(
    private val localTvDataSource: LocalTvShowDataSource,
    private val remoteTvDataSource: RemoteTvShowsDatasource,
    private val tvLocalMapper: TvShowLocalMapper,
    private val tvRemoteMapper: RemoteTvShowMapper,
    private val recentSearchDatasource: LocalRecentSearchDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TvShowRepository {

    override suspend fun getTvShowByKeyword(keyword: String): List<TvShow> {

        return withContext(dispatcher) {
            val recentSearch =
                recentSearchDatasource.getSearchByKeywordAndType(keyword, SearchType.BY_KEYWORD)
            val isExpired = !isSearchExpired(recentSearch)
            if (!isExpired) {
                val localTvShows = localTvDataSource.getTvShowsBySearchKeyword(
                    searchKeyword = keyword
                )
                return@withContext tvLocalMapper.mapListFromLocal(localTvShows)
            }
            deleteRecentSearch(recentSearch)

            val remoteTvShows = remoteTvDataSource.getTvShowsByKeyword(keyword)
            val domainTvShows = tvRemoteMapper.mapResponseToDomain(remoteTvShows)

            localTvDataSource.addAllTvShows(
                tvShows = domainTvShows.map { tvLocalMapper.mapToLocal(it) },
                searchKeyword = keyword,
            )

            domainTvShows
        }
    }

    private suspend fun deleteRecentSearch(
        recentSearch: LocalSearchDto?
    ) {
        recentSearchDatasource.deleteSearchByKeywordAndType(
            recentSearch?.searchKeyword ?: "",
            recentSearch?.searchType ?: SearchType.BY_KEYWORD
        )

    }

    private fun isSearchExpired(recentSearch: LocalSearchDto?): Boolean {
        return recentSearch?.expireDate != null && recentSearch.expireDate < Clock.System.now()
    }
}