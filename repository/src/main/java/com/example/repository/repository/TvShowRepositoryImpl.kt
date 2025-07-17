package com.example.repository.repository

import com.example.domain.repository.TvShowRepository
import com.example.repository.datasource.local.TvShowLocalSource
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.mapper.local.TvShowLocalMapper
import com.example.repository.mapper.remote.TvShowRemoteMapper
import com.example.repository.utils.RecentSearchHandler

class TvShowRepositoryImpl(
    private val localTvDataSource: TvShowLocalSource,
    private val remoteTvDataSource: TvShowsRemoteSource,
    private val tvLocalMapper: TvShowLocalMapper,
    private val tvRemoteMapper: TvShowRemoteMapper,
    private val recentSearchHandler: RecentSearchHandler,
) : TvShowRepository {
    override suspend fun getTvShowByKeyword(keyword: String) =
        getCachedTvShows(keyword)
            ?: recentSearchHandler.deleteRecentSearch(keyword, SearchType.BY_KEYWORD)
                .let { getTvShowsFromRemote(keyword) }
                .let { remoteTvShows ->
                    saveTvShowsToDatabase(remoteTvShows, keyword)
                    tvRemoteMapper.mapToTvShows(remoteTvShows)
                }

    private suspend fun getCachedTvShows(keyword: String) =
        recentSearchHandler.isRecentSearchExpired(keyword, SearchType.BY_KEYWORD)
            .takeIf { isRecentSearchExpired -> !isRecentSearchExpired }
            ?.let { getTvShowFromLocal(keyword, SearchType.BY_KEYWORD) }
            ?.takeIf { tvShows -> tvShows.isNotEmpty() }

    private suspend fun getTvShowFromLocal(keyword: String, searchType: SearchType) =
        tvLocalMapper.mapToTvShows(
            localTvDataSource.getTvShowsByKeywordAndSearchType(
                keyword, searchType
            )
        )

    private suspend fun getTvShowsFromRemote(keyword: String) =
        remoteTvDataSource.getTvShowsByKeyword(keyword)

    private suspend fun saveTvShowsToDatabase(
        remoteTvShows: RemoteTvShowResponse, keyword: String
    ) = localTvDataSource.addTvShows(
        tvRemoteMapper.mapToLocalTvShows(remoteTvShows),
        keyword,
    )
}