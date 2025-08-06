package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Episode
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.AuthenticationLocalSource
import com.amsterdam.repository.datasource.local.CategoryLocalSource
import com.amsterdam.repository.datasource.local.TvShowLocalSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import com.amsterdam.repository.dto.remote.RemoteTvShowResponse
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.local.toEntity
import com.amsterdam.repository.mapper.remote.toEntity
import com.amsterdam.repository.mapper.remote.toEntityList
import com.amsterdam.repository.mapper.remote.toTvShowUserRateEntityList
import com.amsterdam.repository.mapper.remoteToLocal.toLocalDto
import com.amsterdam.repository.mapper.remoteToLocal.toLocalDtoList
import com.amsterdam.repository.mapper.remoteToLocal.toLocalTvShowCategoryDtoList
import com.amsterdam.repository.security.CryptoData
import com.amsterdam.repository.utils.getCachedOrRemoteData
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class TvShowRepositoryImpl @Inject constructor(
    private val localTvDataSource: TvShowLocalSource,
    private val remoteTvDataSource: TvShowsRemoteSource,
    private val authenticationLocalSource: AuthenticationLocalSource,
    private val preferences: AppPreferences,
    private val cryptoData: CryptoData,
    private val categoryLocalSource: CategoryLocalSource,
    private val categoryRemoteSource: CategoryRemoteSource
    ) : TvShowRepository {

    override suspend fun getTvShowByKeyword(
        keyword: String,
        page: Int,
        tvShowsPerPage: Int
    ): List<TvShow> {
        return getTvShows(keyword, page).results.toEntityList()
    }

    override suspend fun getPopularTvShows(): List<TvShow> {
        return getCachedOrRemoteData<TvShowWithCategory, RemoteTvShowItemDto, TvShow>(
            deleteExpired = ::deleteExpiredPopularTvShows,
            getFromLocal = ::getPopularTvShowsFromLocal,
            getFromRemote = ::getPopularTvShowsFromRemote,
            saveRemoteToDatabase = ::savePopularTvShows,
            mapFromLocalToEntity = { it.toEntity() },
            mapFromRemoteToEntity = { it.toEntity() }
        )
    }

    override suspend fun getTopRatedTvShows(page: Int): List<TvShow> {
        return getCachedOrRemoteData<LocalTvShowDto, RemoteTvShowItemDto, TvShow>(
            deleteExpired = ::deleteExpiredTopRatedTvShows,
            getFromLocal = ::getTopRatedTvShowsFromLocal,
            getFromRemote = { getTopRatedTvShowsFromRemote(page) },
            saveRemoteToDatabase = ::saveTopRatedTvShows,
            mapFromLocalToEntity = { it.toEntity() },
            mapFromRemoteToEntity = { it.toEntity() }
        )
    }

    override suspend fun getTvShowCast(tvShowId: Long): List<Actor> {
        return remoteTvDataSource.getTvShowCast(tvShowId).cast.toEntityList()
    }

    override suspend fun getTvShowDetails(tvShowId: Long): GetTvShowDetailsUseCase.TvShowDetails {
        val sessionId = cryptoData.decryptString(authenticationLocalSource.getCachedSessionId()) ?: ""

        return remoteTvDataSource.getTvShowDetailsById(tvShowId, sessionId)
            .also {
                incrementUserInterestByTvShow(it.genres)
                cacheWatchedTvShow(it)
            }.toEntity()

    }

    override suspend fun getTvShowSeasons(tvShowId: Long): List<Season> {
        return remoteTvDataSource.getTvShowDetailsById(tvShowId).seasons.toEntityList()
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): List<Episode> {
        return remoteTvDataSource.getEpisodesBySeasonNumber(
            tvShowId,
            seasonNumber
        ).episodes.map { episodeDto ->
            val videoUrl = getEpisodeVideoUrl(tvShowId, seasonNumber, episodeDto.episodeNumber)
            episodeDto.toEntity(videoUrl)
        }
    }

    override suspend fun getEpisodeVideoUrl(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): String {
        return remoteTvDataSource.getEpisodeVideos(
            tvShowId,
            seasonNumber,
            episodeNumber
        ).results.firstOrNull()?.fullVideoUrl?:""


    }


    private suspend fun cacheWatchedTvShow(remoteTvShowItemDto: TvShowDetailsRemoteResponse) {
        localTvDataSource.insertTvShow(
            remoteTvShowItemDto.toLocalDto(preferences.getAppLanguage().first())
        )
    }
        override suspend fun getUserRatedTvShows(): List<UserRatedTvShow> {
            val sessionId = cryptoData.decryptString(authenticationLocalSource.getCachedSessionId()) ?: ""
            return remoteTvDataSource.getRatedTvShows(sessionId).results.toTvShowUserRateEntityList()
        }

        override suspend fun setTvShowRate(rate: Int, tvShowId: Long) {
            val sessionId =
                cryptoData.decryptString(authenticationLocalSource.getCachedSessionId()) ?: ""
            remoteTvDataSource.setTvShowRate(
                rate = rate,
                tvShowId = tvShowId,
                sessionId = sessionId
            )
        }

        override suspend fun deleteTvShowRate(tvShowId: Long) {
            val sessionId =
                cryptoData.decryptString(authenticationLocalSource.getCachedSessionId()) ?: ""
            remoteTvDataSource.deleteTvShowRate(tvShowId = tvShowId, sessionId = sessionId)

        }

        private suspend fun deleteExpiredPopularTvShows() {
            localTvDataSource.deleteExpiredPopularTvShows(
                expirationTime = Clock.System.now().minus(1.days),
                storedLanguage = preferences.getAppLanguage().first()
            )
        }

        private suspend fun getPopularTvShowsFromLocal(): List<TvShowWithCategory> {
            return localTvDataSource.getPopularTvShows(
                preferences.getAppLanguage().first()
            )
        }

        private suspend fun getPopularTvShowsFromRemote(): List<RemoteTvShowItemDto> {
            return remoteTvDataSource.getPopularTvShows().results
        }

        private suspend fun savePopularTvShows(remoteTvShows: List<RemoteTvShowItemDto>) {
            saveTvShowWithCategories(remoteTvShows).also {
                localTvDataSource.addPopularTvShows(
                    remoteTvShows.toLocalDtoList(preferences.getAppLanguage().first()),
                )
            }
        }

        private suspend fun deleteExpiredTopRatedTvShows() {
            localTvDataSource.deleteExpiredTopRatedTvShows(
                expirationTime = Clock.System.now().minus(1.days),
                storedLanguage = preferences.getAppLanguage().first()
            )
        }

        private suspend fun getTopRatedTvShowsFromLocal(): List<LocalTvShowDto> {
            return localTvDataSource.getTopRatedTvShows(
                preferences.getAppLanguage().first()
            )
        }

        private suspend fun getTopRatedTvShowsFromRemote(page: Int): List<RemoteTvShowItemDto> {
            return remoteTvDataSource.getTopRatedTvShows(page).results
        }

        private suspend fun saveTopRatedTvShows(remoteTvShows: List<RemoteTvShowItemDto>) {
            saveTvShowWithCategories(remoteTvShows).also {
                localTvDataSource.addTopRatedTvShows(
                    remoteTvShows.toLocalDtoList(preferences.getAppLanguage().first()),
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
            cacheTvShowCategoriesIfNotCached()
                localTvDataSource.addTvShowWithCategories(
                    tvShow = remoteTvShow.toLocalDto(preferences.getAppLanguage().first()),
                    categoryIds = remoteTvShow.genreIds.map(Int::toLong),
                    storedLanguage = preferences.getAppLanguage().first()
                )

        }

        private suspend fun incrementUserInterestByTvShow(remoteCategories: List<RemoteCategoryDto>) {
            remoteCategories.map(RemoteCategoryDto::id)
                .map {
                    localTvDataSource.incrementGenreInterest(it.toLong())
                }
        }
    suspend fun cacheTvShowCategoriesIfNotCached(){
        getTvShowCategoriesFromLocal().takeIf { it.isNotEmpty() }
            ?: saveTvShowCategoriesToDatabase(categoryRemoteSource.getTvShowCategories())
    }

    private suspend fun getTvShowCategoriesFromLocal(): List<LocalTvShowCategoryDto> {
        return categoryLocalSource.getTvShowCategories()
    }


    private suspend fun saveTvShowCategoriesToDatabase(
        tvShowCategories: RemoteCategoryResponse
    ) {
        categoryLocalSource.upsertTvShowCategories(
            tvShowCategories.genres.toLocalTvShowCategoryDtoList(preferences.getAppLanguage().first())
        )
    }

}