package com.example.repository.repository

import com.example.domain.repository.TvShowRepository
import com.example.entity.TvShow
import com.example.repository.datasource.local.TvShowLocalSource
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.mapper.local.TvShowLocalMapper
import com.example.repository.mapper.remote.TvShowRemoteMapper
import com.example.repository.utils.RecentSearchHandler
import com.example.repository.utils.tryToExecute
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TvShowRepositoryImpl(
    private val localTvDataSource: TvShowLocalSource,
    private val remoteTvDataSource: TvShowsRemoteSource,
    private val tvLocalMapper: TvShowLocalMapper,
    private val tvRemoteMapper: TvShowRemoteMapper,
    private val recentSearchHandler: RecentSearchHandler,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TvShowRepository {
    override suspend fun getTvShowByKeyword(
        keyword: String,
        page: Int,
    ): List<TvShow> {
        var tvShows: List<TvShow> = emptyList()
        val searchType = SearchType.BY_KEYWORD
        if (!recentSearchHandler.isExpired(keyword, searchType)) {
            tvShows = getTvShowFromLocal(keyword)
        }
        if (tvShows.isNotEmpty()) return tvShows
        recentSearchHandler.deleteRecentSearch(keyword, searchType)
        return getTvShowsFromRemote(keyword, page)
    }

    private suspend fun getTvShowFromLocal(keyword: String): List<TvShow> {
        return tryToExecute(
            function = {
                localTvDataSource.getTvShowsByKeywordAndSearchType(
                    searchKeyword = keyword,
                    searchType = SearchType.BY_KEYWORD
                )
            },
            onSuccess = { localTvShows -> tvLocalMapper.mapToTvShows(localTvShows) },
            onFailure = { emptyList() },
            dispatcher = dispatcher
        )
    }

    private suspend fun getTvShowsFromRemote(
        keyword: String,
        page: Int,
    ): List<TvShow> {
        return tryToExecute(
            function = {
                remoteTvDataSource.getTvShowsByKeyword(keyword, page)
            },
            onSuccess = { remoteTvShows ->
                saveTvShowsToDatabase(remoteTvShows, keyword)
                tvRemoteMapper.mapToTvShows(remoteTvShows)
            },
            onFailure = { aflamiException -> throw aflamiException },
            dispatcher = dispatcher
        )
    }

    private suspend fun saveTvShowsToDatabase(
        remoteTvShows: RemoteTvShowResponse,
        keyword: String
    ) {
        val localTvShows = tvRemoteMapper.mapToLocalTvShows(remoteTvShows)
        tryToExecute(
            function = {
                localTvDataSource.addTvShows(
                    tvShows = localTvShows,
                    searchKeyword = keyword,
                )
            },
            onSuccess = {},
            onFailure = {},
            dispatcher = dispatcher
        )
    }
}