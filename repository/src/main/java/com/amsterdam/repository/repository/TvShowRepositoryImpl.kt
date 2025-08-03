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
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.local.TvShowLocalMapper
import com.amsterdam.repository.mapper.local.TvShowWithCategoryLocalMapper
import com.amsterdam.repository.mapper.remote.CastRemoteMapper
import com.amsterdam.repository.mapper.remote.EpisodeRemoteMapper
import com.amsterdam.repository.mapper.remote.SeasonRemoteMapper
import com.amsterdam.repository.mapper.remote.TvShowDetailsRemoteMapper
import com.amsterdam.repository.mapper.remote.TvShowRemoteMapper
import com.amsterdam.repository.mapper.remoteToLocal.TvShowRemoteDetailsLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.TvShowRemoteLocalMapper
import com.amsterdam.repository.utils.getCachedOrRemoteData
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class TvShowRepositoryImpl @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val localTvDataSource: TvShowLocalSource,
    private val remoteTvDataSource: TvShowsRemoteSource,
    private val tvShowLocalMapper: TvShowLocalMapper,
    private val preferences: AppPreferences,
    private val tvRemoteMapper: TvShowRemoteMapper,
    private val seasonRemoteMapper: SeasonRemoteMapper,
    private val episodeRemoteMapper: EpisodeRemoteMapper,
    private val tvShowRemoteDetailsLocalMapper: TvShowRemoteDetailsLocalMapper,
    private val tvShowDetailsRemoteMapper: TvShowDetailsRemoteMapper,
    private val tvShowWithCategoryLocalMapper: TvShowWithCategoryLocalMapper,
    private val tvShowRemoteLocalMapper: TvShowRemoteLocalMapper,
    private val castRemoteMapper: CastRemoteMapper,
) : TvShowRepository {
    override suspend fun getTvShowByKeyword(
        keyword: String,
        page: Int,
        tvShowsPerPage: Int
    ): List<TvShow> {
        return tvRemoteMapper.toEntityList(getTvShows(keyword, page).results)
    }

    override suspend fun getPopularTvShows(): List<TvShow> {
        return getCachedOrRemoteData(
            deleteExpired = ::deleteExpiredPopularTvShows,
            getFromLocal = ::getPopularTvShowsFromLocal,
            getFromRemote = ::getPopularTvShowsFromRemote,
            saveRemoteToDatabase = ::savePopularTvShows,
            mapFromLocalToEntity = tvShowWithCategoryLocalMapper::toEntity,
            mapFromRemoteToEntity = tvRemoteMapper::toEntity
        )
    }

    override suspend fun getTopRatedTvShows(page: Int): List<TvShow> {
        return getCachedOrRemoteData(
            deleteExpired = ::deleteExpiredTopRatedTvShows,
            getFromLocal = ::getTopRatedTvShowsFromLocal,
            getFromRemote = { getTopRatedTvShowsFromRemote(page) },
            saveRemoteToDatabase = ::saveTopRatedTvShows,
            mapFromLocalToEntity = tvShowLocalMapper::toEntity,
            mapFromRemoteToEntity = tvRemoteMapper::toEntity
        )
    }

    override suspend fun getTvShowCast(tvShowId: Long): List<Actor> {
        return castRemoteMapper.toEntityList(remoteTvDataSource.getTvShowCast(tvShowId).cast)
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

    private suspend fun cacheWatchedTvShow(remoteTvShowItemDto: TvShowDetailsRemoteResponse) {
        localTvDataSource.insertTvShow(
            tvShowRemoteDetailsLocalMapper.toLocal(
                remote = remoteTvShowItemDto, args = listOf(preferences.getDeviceLanguage().first())
            )
        )
    }

    private suspend fun deleteExpiredPopularTvShows() {
        localTvDataSource.deleteExpiredPopularTvShows(
            expirationTime = Clock.System.now().minus(1.days),
            storedLanguage = preferences.getDeviceLanguage().first()
        )
    }

    private suspend fun getPopularTvShowsFromLocal(): List<TvShowWithCategory> {
        return localTvDataSource.getPopularTvShows(
            preferences.getDeviceLanguage().first()
        )
    }

    private suspend fun getPopularTvShowsFromRemote(): List<RemoteTvShowItemDto> {
        return remoteTvDataSource.getPopularTvShows().results
    }

    private suspend fun savePopularTvShows(remoteTvShows: List<RemoteTvShowItemDto>) {
        saveTvShowWithCategories(remoteTvShows).also {
            localTvDataSource.addPopularTvShows(
                tvShowRemoteLocalMapper.toLocalList(
                    remoteTvShows,
                    listOf(preferences.getDeviceLanguage().first())
                )
            )
        }
    }

    private suspend fun deleteExpiredTopRatedTvShows() {
        localTvDataSource.deleteExpiredTopRatedTvShows(
            expirationTime = Clock.System.now().minus(1.days),
            storedLanguage = preferences.getDeviceLanguage().first()
        )
    }

    private suspend fun getTopRatedTvShowsFromLocal(): List<LocalTvShowDto> {
        return localTvDataSource.getTopRatedTvShows(
            preferences.getDeviceLanguage().first()
        )
    }

    private suspend fun getTopRatedTvShowsFromRemote(page: Int): List<RemoteTvShowItemDto> {
        return remoteTvDataSource.getTopRatedTvShows(page).results
    }

    private suspend fun saveTopRatedTvShows(remoteTvShows: List<RemoteTvShowItemDto>) {
        saveTvShowWithCategories(remoteTvShows).also {
            localTvDataSource.addTopRatedTvShows(
                tvShowRemoteLocalMapper.toLocalList(
                    remoteTvShows,
                    listOf(preferences.getDeviceLanguage().first())
                )
            )
        }
    }

    private suspend fun getTvShows(keyword: String, page: Int): RemoteTvShowResponse {
        return remoteTvDataSource.getTvShowsByKeyword(keyword, page)
    }

    private suspend fun saveTvShowWithCategories(remoteTvShows: List<RemoteTvShowItemDto>) {
        remoteTvShows.forEach { onSaveTvShowWithCategories(it) }
    }

    private suspend fun onSaveTvShowWithCategories(remoteTvShow: RemoteTvShowItemDto) {
        categoryRepository.getTvShowCategories().also {
            localTvDataSource.addTvShowWithCategories(
                tvShow = tvShowRemoteLocalMapper.toLocal(
                    remoteTvShow,
                    listOf(preferences.getDeviceLanguage().first())
                ),
                categoryIds = remoteTvShow.genreIds.map(Int::toLong),
                storedLanguage = preferences.getDeviceLanguage().first()
            )
        }
    }

    private suspend fun incrementUserInterestByTvShow(remoteCategories: List<RemoteCategoryDto>) {
        remoteCategories.map(RemoteCategoryDto::id)
            .map {
                localTvDataSource.incrementGenreInterest(it.toLong())
            }
    }
}