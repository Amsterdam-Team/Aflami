package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CategoryRepository
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Episode
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.TvShowLocalSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import com.amsterdam.repository.dto.local.utils.SearchType
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.local.TvShowWithCategoryLocalMapper
import com.amsterdam.repository.mapper.remote.CastRemoteMapper
import com.amsterdam.repository.mapper.remote.EpisodeRemoteMapper
import com.amsterdam.repository.mapper.remote.SeasonRemoteMapper
import com.amsterdam.repository.mapper.remote.TvShowDetailsRemoteMapper
import com.amsterdam.repository.mapper.remote.TvShowRemoteMapper
import com.amsterdam.repository.mapper.remoteToLocal.TvShowGenreIdsRemoteLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.TvShowRemoteDetailsLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.TvShowRemoteLocalMapper
import com.amsterdam.repository.utils.RecentSearchHandler
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TvShowRepositoryImpl @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val localTvDataSource: TvShowLocalSource,
    private val remoteTvDataSource: TvShowsRemoteSource,
    private val preferences: AppPreferences,
    private val tvShowGenreIdsRemoteLocalMapper: TvShowGenreIdsRemoteLocalMapper,
    private val tvRemoteMapper: TvShowRemoteMapper,
    private val recentSearchHandler: RecentSearchHandler,
    private val seasonRemoteMapper: SeasonRemoteMapper,
    private val episodeRemoteMapper: EpisodeRemoteMapper,
    private val tvShowWithCategoryLocalMapper: TvShowWithCategoryLocalMapper,
    private val tvShowRemoteLocalMapper: TvShowRemoteLocalMapper,
    private val tvShowRemoteDetailsLocalMapper: TvShowRemoteDetailsLocalMapper,
    private val tvShowDetailsRemoteMapper: TvShowDetailsRemoteMapper,
    private val castRemoteMapper: CastRemoteMapper,
) : TvShowRepository {
    override suspend fun getPopularTvShows(): List<TvShow> {
        return tvRemoteMapper.toEntityList(remoteTvDataSource.getPopularTvShows().results)
    }


    override suspend fun getTvShowCast(tvShowId: Long): List<Actor> {
        return remoteTvDataSource.getTvShowCast(tvShowId).cast.map { castRemoteMapper.toEntity(it) }
    }

    override suspend fun getTvShowByKeyword(
        keyword: String,
        page: Int,
        tvShowsPerPage: Int
    ): List<TvShow> {
        categoryRepository.getTvShowCategories()
        return getCachedTvShows(keyword, page, tvShowsPerPage)
            ?: recentSearchHandler.deleteRecentSearch(
                keyword,
                SearchType.BY_KEYWORD,
                preferences.getDeviceLanguage().first()
            )
                .let { getTvShowsFromRemote(keyword, page) }
                .let { remoteTvShows ->
                    saveTvShowsToDatabase(remoteTvShows, keyword)
                        .let { getTvShowFromLocal(keyword, page, tvShowsPerPage) }
                        .takeIf { tvShows -> tvShows.isNotEmpty() }
                        ?: tvRemoteMapper.toEntityList(remoteTvShows.results)
                }
    }

    private suspend fun getCachedTvShows(
        keyword: String,
        page: Int,
        tvShowsPerPage: Int
    ): List<TvShow>? {
        return recentSearchHandler.isRecentSearchExpired(
            keyword,
            SearchType.BY_KEYWORD,
            preferences.getDeviceLanguage().first()
        )
            .takeIf { isRecentSearchExpired -> !isRecentSearchExpired }
            ?.let { getTvShowFromLocal(keyword, page, tvShowsPerPage) }
            ?.takeIf { tvShows -> tvShows.isNotEmpty() }
    }

    override suspend fun getTvShowDetails(tvShowId: Long): GetTvShowDetailsUseCase.TvShowDetails {
        return tvShowDetailsRemoteMapper.toEntity(
            remoteTvDataSource.getTvShowDetailsById(tvShowId)
                .also {
                    incrementUserInterestByTvShow(it.genres)
                    cacheWatchedTvShow(it)
                }
        )
    }


    private suspend fun cacheWatchedTvShow(remoteTvShowItemDto: TvShowDetailsRemoteResponse) {
        localTvDataSource.insertTvShow(
            tvShowRemoteDetailsLocalMapper.toLocal(
                remote = remoteTvShowItemDto, args = listOf(preferences.getDeviceLanguage().first())
            )
        )
    }

    override suspend fun getTvShowSeasons(tvShowId: Long): List<Season> {
        return seasonRemoteMapper.toEntityList(remoteTvDataSource.getTvShowDetailsById(tvShowId).seasons)
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): List<Episode> {
        return episodeRemoteMapper.toEntityList(
            remoteTvDataSource.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            ).episodes
        )
    }

    override suspend fun getTopRatedTvShows(page: Int): List<TvShow> {
        return tvRemoteMapper.toEntityList(remoteTvDataSource.getTopRatedTvShows(page).results)
    }

    private suspend fun getTvShowFromLocal(
        keyword: String,
        page: Int,
        tvShowsPerPage: Int
    ): List<TvShow> {
        return try {
            tvShowWithCategoryLocalMapper.toEntityList(
                localTvDataSource.getTvShowsBySearchKeywordSortedByInterest(
                    searchKeyword = keyword,
                    storedLanguage = preferences.getDeviceLanguage().first(),
                    limit = tvShowsPerPage,
                    offset = tvShowsPerPage * (page - 1)
                )
            )
        } catch (_: Exception) {
            emptyList()
        }
    }

    private suspend fun getTvShowsFromRemote(keyword: String, page: Int): RemoteTvShowResponse {
        return remoteTvDataSource.getTvShowsByKeyword(keyword, page).also { remoteTvShowResponse ->
            saveTvShowWithCategories(remoteTvShowResponse)
        }
    }

    private suspend fun saveTvShowsToDatabase(
        remoteTvShows: RemoteTvShowResponse, keyword: String
    ) {
        localTvDataSource.addTvShows(
            tvShowRemoteLocalMapper.toLocalList(
                remoteTvShows.results,
                listOf(preferences.getDeviceLanguage().first())
            ),
            keyword,
            storedLanguage = preferences.getDeviceLanguage().first()
        )
    }

    private suspend fun saveTvShowWithCategories(remoteTvShow: RemoteTvShowResponse) {
        remoteTvShow.results.forEach { onSaveTvShowWithCategories(it) }
    }

    private suspend fun onSaveTvShowWithCategories(remoteTvShow: RemoteTvShowItemDto) {
        localTvDataSource.addTvShowWithCategories(
            tvShow = tvShowRemoteLocalMapper.toLocal(
                remoteTvShow,
                listOf(preferences.getDeviceLanguage().first())
            ),
            categories = tvShowGenreIdsRemoteLocalMapper.toLocalList(
                remoteTvShow.genreIds,
                listOf(preferences.getDeviceLanguage().first())
            ),
            storedLanguage = preferences.getDeviceLanguage().first()
        )
    }

    private suspend fun incrementUserInterestByTvShow(remoteCategories: List<RemoteCategoryDto>) {
        remoteCategories.map(RemoteCategoryDto::id)
            .map { localTvDataSource.incrementGenreInterest(it.toLong()) }
    }
}