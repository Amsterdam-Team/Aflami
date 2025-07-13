package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.TvShow
import com.amsterdam.repository.datasource.local.LocalRecentSearchDataSource
import com.amsterdam.repository.datasource.local.LocalTvShowDataSource
import com.amsterdam.repository.datasource.remote.RemoteTvShowsDatasource
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.dto.local.utils.SearchType
import com.amsterdam.repository.mapper.local.TvShowLocalMapper
import com.amsterdam.repository.mapper.remote.RemoteTvShowMapper
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
        return withContext(dispatcher){
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
            val remoteTvShows = remoteTvDataSource.getTvShowsByKeyword(keyword, 0, 0L)
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