package com.example.repository.repository

import com.example.domain.repository.TvShowRepository
import com.example.entity.TvShow
import com.example.entity.category.TvShowGenre
import com.example.repository.datasource.local.TvShowLocalSource
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.mapper.local.TvShowWithCategoryLocalMapper
import com.example.repository.mapper.remote.TvShowRemoteMapper
import com.example.repository.mapper.remoteToLocal.TvShowRemoteLocalMapper
import com.example.repository.utils.RecentSearchHandler

class TvShowRepositoryImpl(
    private val localTvDataSource: TvShowLocalSource,
    private val remoteTvDataSource: TvShowsRemoteSource,
    private val tvRemoteMapper: TvShowRemoteMapper,
    private val recentSearchHandler: RecentSearchHandler,
    private val tvShowWithCategoryLocalMapper: TvShowWithCategoryLocalMapper,
    private val tvShowRemoteLocalMapper: TvShowRemoteLocalMapper
) : TvShowRepository {
    override suspend fun getTvShowByKeyword(keyword: String): List<TvShow> {
        return getCachedTvShows(keyword)
            ?: recentSearchHandler.deleteRecentSearch(keyword, SearchType.BY_KEYWORD)
                .let { getTvShowsFromRemote(keyword) }
                .let { remoteTvShows ->
                    saveTvShowsToDatabase(remoteTvShows, keyword)
                    tvRemoteMapper.toEntityList(remoteTvShows.results)
                }
    }

    override suspend fun incrementGenreInterest(genre: TvShowGenre) {
        localTvDataSource.incrementGenreInterest(genre)
    }

    override suspend fun getAllGenreInterests(): Map<TvShowGenre, Int> {
        return localTvDataSource.getAllGenreInterests()
    }

    private suspend fun getCachedTvShows(keyword: String): List<TvShow>? {
        return recentSearchHandler.isRecentSearchExpired(keyword, SearchType.BY_KEYWORD)
            .takeIf { isRecentSearchExpired -> !isRecentSearchExpired }
            ?.let { getTvShowFromLocal(keyword, SearchType.BY_KEYWORD) }
            ?.takeIf { tvShows -> tvShows.isNotEmpty() }
    }

    private suspend fun getTvShowFromLocal(keyword: String, searchType: SearchType): List<TvShow> {
        return tvShowWithCategoryLocalMapper.toEntityList(
            localTvDataSource.getTvShowsByKeywordAndSearchType(
                keyword, searchType
            )
        )
    }

    private suspend fun getTvShowsFromRemote(keyword: String): RemoteTvShowResponse {
        return remoteTvDataSource.getTvShowsByKeyword(keyword)
    }

    private suspend fun saveTvShowsToDatabase(
        remoteTvShows: RemoteTvShowResponse, keyword: String
    ) {
        localTvDataSource.addTvShows(
            tvShowRemoteLocalMapper.toLocalList(remoteTvShows.results),
            keyword,
        )
    }
}