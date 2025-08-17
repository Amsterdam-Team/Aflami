package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Episode
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.repository.datasource.local.AppLocalPreferences
import com.amsterdam.repository.datasource.local.CategoryLocalDataSource
import com.amsterdam.repository.datasource.local.TvShowLocalDataSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteDataSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteDataSource
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategories
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.amsterdam.repository.dto.remote.CategoryRemoteResponse
import com.amsterdam.repository.dto.remote.EpisodeRemoteDto
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.TvShowItemRemoteDto
import com.amsterdam.repository.dto.remote.TvShowRemoteResponse
import com.amsterdam.repository.mapper.toDto
import com.amsterdam.repository.mapper.toEntity
import com.amsterdam.repository.mapper.toEntityList
import com.amsterdam.repository.mapper.toLocalDto
import com.amsterdam.repository.mapper.toLocalTvShowDtoList
import com.amsterdam.repository.mapper.toTvShowUserRateEntityList
import com.amsterdam.repository.utils.getCachedOrRemoteData
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class TvShowRepositoryImpl @Inject constructor(
    private val localTvDataSource: TvShowLocalDataSource,
    private val remoteTvDataSource: TvShowsRemoteDataSource,
    private val preferences: AppLocalPreferences,
    private val categoryLocalDataSource: CategoryLocalDataSource,
    private val categoryRemoteDataSource: CategoryRemoteDataSource
) : TvShowRepository {
    override suspend fun getTvShowByKeyword(
        keyword: String,
        page: Int,
        tvShowsPerPage: Int
    ): List<TvShow> {
        return getTvShows(keyword, page).results.toEntityList()
    }

    override suspend fun getPopularTvShows(): List<TvShow> {
        return getCachedOrRemoteData<TvShowWithCategories, TvShowItemRemoteDto, TvShow>(
            deleteExpired = ::deleteExpiredPopularTvShows,
            getFromLocal = ::getPopularTvShowsFromLocal,
            getFromRemote = ::getPopularTvShowsFromRemote,
            saveRemoteToDatabase = ::savePopularTvShows,
            mapFromLocalToEntity = { it.toEntity() },
            mapFromRemoteToEntity = { it.toEntity() }
        )
    }

    override suspend fun getTopRatedTvShows(page: Int): List<TvShow> {
        return if (page > 1) getTopRatedTvShowsFromRemote(page).toEntityList()
        else getCachedOrRemoteData<TvShowLocalDto, TvShowItemRemoteDto, TvShow>(
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
        return remoteTvDataSource.getTvShowDetailsById(tvShowId)
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
        ).episodes.map(EpisodeRemoteDto::toEntity)
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
        ).results.firstOrNull()?.fullVideoUrl ?: ""


    }

    override suspend fun getTvShowsByGenre(
        tvShowGenre: TvShowGenre,
        page: Int
    ): List<TvShow> {
        return tvShowGenre.toDto().let { genreId ->
            remoteTvDataSource.getTvShowsByGenreId(
                genreId,
                page
            ).results.toEntityList()
        }
    }


    private suspend fun cacheWatchedTvShow(remoteTvShowItemDto: TvShowDetailsRemoteResponse) {
        localTvDataSource.upsertTvShow(
            remoteTvShowItemDto.toLocalDto(preferences.getAppLanguage().first())
        )
    }

    override suspend fun getUserRatedTvShows(): List<UserRatedTvShow> {
        return remoteTvDataSource.getRatedTvShows().results.toTvShowUserRateEntityList()
    }

    override suspend fun setTvShowRate(rate: Int, tvShowId: Long) {
        remoteTvDataSource.setTvShowRate(
            rate = rate,
            tvShowId = tvShowId,
        )
    }

    override suspend fun deleteTvShowRate(tvShowId: Long) {
        remoteTvDataSource.deleteTvShowRate(tvShowId = tvShowId)

    }

    private suspend fun deleteExpiredPopularTvShows() {
        localTvDataSource.deleteExpiredPopularTvShows(
            expirationTime = Clock.System.now().minus(1.days),
            storedLanguage = preferences.getAppLanguage().first()
        )
    }

    private suspend fun getPopularTvShowsFromLocal(): List<TvShowWithCategories> {
        return localTvDataSource.getPopularTvShows(
            preferences.getAppLanguage().first()
        )
    }

    private suspend fun getPopularTvShowsFromRemote(): List<TvShowItemRemoteDto> {
        return remoteTvDataSource.getPopularTvShows().results
    }

    private suspend fun savePopularTvShows(remoteTvShows: List<TvShowItemRemoteDto>) {
        saveTvShowWithCategories(remoteTvShows).also {
            localTvDataSource.upsertPopularTvShows(
                remoteTvShows.toLocalTvShowDtoList(preferences.getAppLanguage().first()),
            )
        }
    }

    private suspend fun deleteExpiredTopRatedTvShows() {
        localTvDataSource.deleteExpiredTopRatedTvShows(
            expirationTime = Clock.System.now().minus(1.days),
            storedLanguage = preferences.getAppLanguage().first()
        )
    }

    private suspend fun getTopRatedTvShowsFromLocal(): List<TvShowLocalDto> {
        return localTvDataSource.getTopRatedTvShows(
            preferences.getAppLanguage().first()
        )
    }

    private suspend fun getTopRatedTvShowsFromRemote(page: Int): List<TvShowItemRemoteDto> {
        return remoteTvDataSource.getTopRatedTvShows(page).results
    }

    private suspend fun saveTopRatedTvShows(remoteTvShows: List<TvShowItemRemoteDto>) {
        saveTvShowWithCategories(remoteTvShows).also {
            localTvDataSource.upsertTopRatedTvShows(
                remoteTvShows.toLocalTvShowDtoList(preferences.getAppLanguage().first()),
            )
        }
    }

    private suspend fun getTvShows(keyword: String, page: Int): TvShowRemoteResponse {
        return remoteTvDataSource.getTvShowsByKeyword(keyword, page)
    }

    private suspend fun saveTvShowWithCategories(remoteTvShows: List<TvShowItemRemoteDto>) {
        remoteTvShows.forEach { onSaveTvShowWithCategories(it) }
    }

    private suspend fun onSaveTvShowWithCategories(remoteTvShow: TvShowItemRemoteDto) {
        cacheTvShowCategoriesIfNotCached()
        localTvDataSource.upsertTvShowWithCategories(
            tvShow = remoteTvShow.toLocalDto(preferences.getAppLanguage().first()),
            categoryIds = remoteTvShow.genreIds.map(Int::toLong),
            storedLanguage = preferences.getAppLanguage().first()
        )

    }

    private suspend fun incrementUserInterestByTvShow(remoteCategories: List<CategoryRemoteDto>) {
        remoteCategories.map(CategoryRemoteDto::id)
            .map { localTvDataSource.incrementGenreInterest(it.toLong()) }
    }

    suspend fun cacheTvShowCategoriesIfNotCached() {
        getTvShowCategoriesFromLocal().takeIf { it.isNotEmpty() }
            ?: saveTvShowCategoriesToDatabase(categoryRemoteDataSource.getTvShowCategories())
    }

    private suspend fun getTvShowCategoriesFromLocal(): List<TvShowCategoryLocalDto> {
        return categoryLocalDataSource.getTvShowCategories()
    }


    private suspend fun saveTvShowCategoriesToDatabase(
        tvShowCategories: CategoryRemoteResponse
    ) {
        categoryLocalDataSource.upsertTvShowCategories(
            tvShowCategories.genres.toLocalTvShowDtoList(
                preferences.getAppLanguage().first()
            )
        )
    }
}