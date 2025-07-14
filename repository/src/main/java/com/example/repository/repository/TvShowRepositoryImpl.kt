package com.example.repository.repository

import com.example.domain.repository.TvShowRepository
import com.example.domain.useCase.genreTypes.TvShowGenre
import com.example.entity.TvShow
import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.datasource.local.TvShowLocalSource
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.mapper.local.TvShowLocalMapper
import com.example.repository.mapper.remote.GenreMapper
import com.example.repository.mapper.remote.RemoteTvShowMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class TvShowRepositoryImpl(
    private val localTvDataSource: TvShowLocalSource,
    private val remoteTvDataSource: TvShowsRemoteSource,
    private val tvLocalMapper: TvShowLocalMapper,
    private val tvRemoteMapper: RemoteTvShowMapper,
    private val genreMapper: GenreMapper,
    private val recentSearchDatasource: RecentSearchLocalSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) : TvShowRepository {
    override suspend fun getTvShowByKeyword(keyword: String, rating: Float, tvShowGenre: TvShowGenre): List<TvShow> {
        return withContext(dispatcher){
            val recentSearch =
                recentSearchDatasource.getSearchByKeywordAndType(keyword, SearchType.BY_KEYWORD)
            val isExpired = !isSearchExpired(recentSearch)
            if (!isExpired) {
                val localTvShows = localTvDataSource.getTvShowsBy(
                    searchKeyword = keyword
                )
                return@withContext tvLocalMapper.mapToTvShows(localTvShows)
            }
            deleteRecentSearch(recentSearch)

            val remoteTvShows = if (rating != 0f || tvShowGenre != TvShowGenre.ALL) {
                remoteTvDataSource.discoverTvShows(keyword, rating, genreMapper.mapToTvShowGenreId(tvShowGenre))
            } else {
                remoteTvDataSource.getTvShowsByKeyword(keyword)
            }

            val domainTvShows = tvRemoteMapper.mapToTvShows(remoteTvShows)

            localTvDataSource.addTvShows(
                tvShows = domainTvShows.map { tvLocalMapper.mapToLocalTvShow(it) },
                searchKeyword = keyword,
            )

            domainTvShows
        }
    }

    private suspend fun deleteRecentSearch(
        recentSearch: LocalSearchDto?
    ) {
        recentSearchDatasource.deleteRecentSearchByKeywordAndType(
            recentSearch?.searchKeyword ?: "",
            recentSearch?.searchType ?: SearchType.BY_KEYWORD
        )

    }

    private fun isSearchExpired(recentSearch: LocalSearchDto?): Boolean {
        return recentSearch?.expireDate != null && recentSearch.expireDate < Clock.System.now()
    }
}