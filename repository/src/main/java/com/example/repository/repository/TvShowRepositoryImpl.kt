package com.example.repository.repository

import com.example.domain.repository.TvShowRepository
import com.example.entity.Actor
import com.example.entity.Episode
import com.example.entity.ProductionCompany
import com.example.entity.Review
import com.example.entity.Season
import com.example.entity.TvShow
import com.example.repository.datasource.local.TvShowLocalSource
import com.example.repository.datasource.remote.TvShowsRemoteSource
import com.example.repository.dto.local.utils.SearchType
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.mapper.local.TvShowWithCategoryLocalMapper
import com.example.repository.mapper.remote.CastRemoteMapper
import com.example.repository.mapper.remote.EpisodeRemoteMapper
import com.example.repository.mapper.remote.GalleryRemoteMapper
import com.example.repository.mapper.remote.ProductionCompanyRemoteMapper
import com.example.repository.mapper.remote.ReviewRemoteMapper
import com.example.repository.mapper.remote.SeasonRemoteMapper
import com.example.repository.mapper.remote.TvShowRemoteMapper
import com.example.repository.mapper.remoteToLocal.TvShowRemoteLocalMapper
import com.example.repository.utils.RecentSearchHandler
import com.example.repository.utils.tryToExecute

class TvShowRepositoryImpl(
    private val localTvDataSource: TvShowLocalSource,
    private val remoteTvDataSource: TvShowsRemoteSource,
    private val tvRemoteMapper: TvShowRemoteMapper,
    private val recentSearchHandler: RecentSearchHandler,
    private val castRemoteMapper: CastRemoteMapper,
    private val reviewRemoteMapper: ReviewRemoteMapper,
    private val galleryRemoteMapper: GalleryRemoteMapper,
    private val remoteProductionCompanyMapper: ProductionCompanyRemoteMapper,
    private val seasonRemoteMapper: SeasonRemoteMapper,
    private val episodeRemoteMapper: EpisodeRemoteMapper,
    private val tvShowWithCategoryLocalMapper: TvShowWithCategoryLocalMapper,
    private val tvShowRemoteLocalMapper: TvShowRemoteLocalMapper
) : TvShowRepository {
    override suspend fun getTvShowByKeyword(keyword: String): List<TvShow> {
        var tvShows: List<TvShow> = emptyList()
        val searchType = SearchType.BY_KEYWORD
        if (!recentSearchHandler.isExpired(keyword, searchType)) {
            tvShows = getTvShowFromLocal(keyword)
        }
        if (tvShows.isNotEmpty()) return tvShows
        recentSearchHandler.deleteRecentSearch(keyword, searchType)
        return getTvShowsFromRemote(keyword)
    }

    override suspend fun getTvShowDetails(tvShowId: Long): TvShow {
        return tvRemoteMapper.toEntity(remoteTvDataSource.getTvShowDetailsById(tvShowId))
    }

    override suspend fun getTvShowCast(tvShowId: Long): List<Actor> {
        return remoteTvDataSource.getTvShowCast(tvShowId).cast.map { castRemoteMapper.toEntity(it) }
    }

    override suspend fun getTvShowSeasons(tvShowId: Long): List<Season> {
        return seasonRemoteMapper.mapToSeasons(remoteTvDataSource.getTvShowDetailsById(tvShowId))
    }

    override suspend fun getEpisodesBySeasonNumber(
        tvShowId: Long,
        seasonNumber: Int
    ): List<Episode> {
        return episodeRemoteMapper.mapToEpisodes(
            remoteTvDataSource.getEpisodesBySeasonNumber(
                tvShowId,
                seasonNumber
            )
        )
    }

    override suspend fun getTvShowReviews(tvShowId: Long): List<Review> {
        return reviewRemoteMapper.toEntityList(remoteTvDataSource.getTvShowReviews(tvShowId).results)
    }

    override suspend fun getSimilarTvShows(tvShowId: Long): List<TvShow> {
        return tvRemoteMapper.toEntityList(remoteTvDataSource.getSimilarTvShows(tvShowId).results)
    }

    override suspend fun getTvShowGallery(tvShowId: Long): List<String> {
        return galleryRemoteMapper.toEntity(remoteTvDataSource.getTvShowGallery(tvShowId))
    }

    override suspend fun getProductionCompany(tvShowId: Long): List<ProductionCompany> {
        return remoteProductionCompanyMapper.toEntityList(
            remoteTvDataSource.getTvShowCompanyProduction(tvShowId).productionCompanies
        )
    }

    private suspend fun getTvShowFromLocal(keyword: String): List<TvShow> {
        return tryToExecute(
            function = {
                localTvDataSource.getTvShowsByKeywordAndSearchType(
                    searchKeyword = keyword,
                    searchType = SearchType.BY_KEYWORD
                )
            },
            onSuccess = { localTvShows -> tvShowWithCategoryLocalMapper.toEntityList(localTvShows) },
            onFailure = { emptyList() },
        )
    }

    private suspend fun getTvShowsFromRemote(
        keyword: String,
    ): List<TvShow> {
        return tryToExecute(
            function = {
                remoteTvDataSource.getTvShowsByKeyword(keyword)
            },
            onSuccess = { remoteTvShows ->
                saveTvShowsToDatabase(remoteTvShows, keyword)
                tvRemoteMapper.toEntityList(remoteTvShows.results)
            },
            onFailure = { aflamiException -> throw aflamiException },
        )
    }

    private suspend fun saveTvShowsToDatabase(
        remoteTvShows: RemoteTvShowResponse,
        keyword: String
    ) {
        val localTvShows = tvShowRemoteLocalMapper.toLocalList(remoteTvShows.results)
        tryToExecute(
            function = {
                localTvDataSource.addTvShows(
                    tvShows = localTvShows,
                    searchKeyword = keyword,
                )
            },
            onSuccess = {},
            onFailure = {},
        )
    }
}