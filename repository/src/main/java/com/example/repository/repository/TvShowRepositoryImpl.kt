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

class TvShowRepositoryImpl(
    private val localTvDataSource: TvShowLocalSource,
    private val remoteTvDataSource: TvShowsRemoteSource,
    private val tvLocalMapper: TvShowLocalMapper,
    private val tvRemoteMapper: TvShowRemoteMapper,
    private val recentSearchHandler: RecentSearchHandler,
) : TvShowRepository {

    override suspend fun getTvShowByKeyword(keyword: String): List<TvShow> =
        recentSearchHandler.isRecentSearchExpired(keyword, SearchType.BY_KEYWORD)
            .takeIf { isRecentSearchExpired -> !isRecentSearchExpired }
            ?.let { getTvShowFromLocal(keyword, SearchType.BY_KEYWORD) }
            ?: recentSearchHandler.deleteRecentSearch(keyword, SearchType.BY_KEYWORD)
                .let { getTvShowsFromRemote(keyword) }


    private suspend fun getTvShowFromLocal(keyword: String, searchType: SearchType): List<TvShow> =
        tryToExecute(
            function = {
                localTvDataSource.getTvShowsByKeywordAndSearchType(keyword, searchType)
            },
            onSuccess = { localTvShows -> tvLocalMapper.mapToTvShows(localTvShows) },
            onFailure = { emptyList() },
        )

    private suspend fun getTvShowsFromRemote(
        keyword: String,
    ): List<TvShow> = tryToExecute(
        function = { remoteTvDataSource.getTvShowsByKeyword(keyword) },
            onSuccess = { remoteTvShows ->
                saveTvShowsToDatabase(remoteTvShows, keyword)
                tvRemoteMapper.mapToTvShows(remoteTvShows)
            },
            onFailure = { aflamiException -> throw aflamiException },
        )

    private suspend fun saveTvShowsToDatabase(
        remoteTvShows: RemoteTvShowResponse,
        keyword: String
    ) = tryToExecute(
            function = {
                localTvDataSource.addTvShows(
                    tvRemoteMapper.mapToLocalTvShows(remoteTvShows),
                    keyword,
                )
            },
            onSuccess = {},
            onFailure = {},
        )
}