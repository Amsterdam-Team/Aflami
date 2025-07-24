package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
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
import com.example.repository.dto.remote.RemoteCategoryDto
import com.example.repository.dto.remote.RemoteTvShowItemDto
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.example.repository.mapper.local.TvShowWithCategoryLocalMapper
import com.example.repository.mapper.remote.CastRemoteMapper
import com.example.repository.mapper.remote.EpisodeRemoteMapper
import com.example.repository.mapper.remote.GalleryRemoteMapper
import com.example.repository.mapper.remote.ProductionCompanyRemoteMapper
import com.example.repository.mapper.remote.ReviewRemoteMapper
import com.example.repository.mapper.remote.SeasonRemoteMapper
import com.example.repository.mapper.remote.TvShowDetailsRemoteMapper
import com.example.repository.mapper.remote.TvShowRemoteMapper
import com.example.repository.mapper.remoteToLocal.TvShowGenreIdsRemoteLocalMapper
import com.example.repository.mapper.remoteToLocal.TvShowRemoteLocalMapper
import com.example.repository.utils.RecentSearchHandler
import com.example.repository.utils.getDeviceLanguage

class TvShowRepositoryImpl(
    private val categoryRepository: CategoryRepository,
    private val localTvDataSource: TvShowLocalSource,
    private val remoteTvDataSource: TvShowsRemoteSource,
    private val tvShowGenreIdsRemoteLocalMapper: TvShowGenreIdsRemoteLocalMapper,
    private val tvRemoteMapper: TvShowRemoteMapper,
    private val recentSearchHandler: RecentSearchHandler,
    private val castRemoteMapper: CastRemoteMapper,
    private val reviewRemoteMapper: ReviewRemoteMapper,
    private val galleryRemoteMapper: GalleryRemoteMapper,
    private val remoteProductionCompanyMapper: ProductionCompanyRemoteMapper,
    private val seasonRemoteMapper: SeasonRemoteMapper,
    private val episodeRemoteMapper: EpisodeRemoteMapper,
    private val tvShowWithCategoryLocalMapper: TvShowWithCategoryLocalMapper,
    private val tvShowRemoteLocalMapper: TvShowRemoteLocalMapper,
    private val tvShowDetailsRemoteMapper: TvShowDetailsRemoteMapper
) : TvShowRepository {
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
                getDeviceLanguage()
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
            getDeviceLanguage()
        )
            .takeIf { isRecentSearchExpired -> !isRecentSearchExpired }
            ?.let { getTvShowFromLocal(keyword, page, tvShowsPerPage) }
            ?.takeIf { tvShows -> tvShows.isNotEmpty() }
    }

    override suspend fun getTvShowDetails(tvShowId: Long): TvShow {
        return tvShowDetailsRemoteMapper.toEntity(
            remoteTvDataSource.getTvShowDetailsById(tvShowId)
                .also { incrementUserInterestByTvShow(it.genres) }
        )
    }

    override suspend fun getTvShowCast(tvShowId: Long): List<Actor> {
        return remoteTvDataSource.getTvShowCast(tvShowId).cast.map { castRemoteMapper.toEntity(it) }
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

    private suspend fun getTvShowFromLocal(
        keyword: String,
        page: Int,
        tvShowsPerPage: Int
    ): List<TvShow> {
        return try {
            tvShowWithCategoryLocalMapper.toEntityList(
                localTvDataSource.getTvShowsBySearchKeywordSortedByInterest(
                    searchKeyword = keyword,
                    storedLanguage = getDeviceLanguage(),
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
            tvShowRemoteLocalMapper.toLocalList(remoteTvShows.results, listOf(getDeviceLanguage())),
            keyword,
            storedLanguage = getDeviceLanguage()
        )
    }

    private suspend fun saveTvShowWithCategories(remoteTvShow: RemoteTvShowResponse) {
        remoteTvShow.results.forEach { onSaveTvShowWithCategories(it) }
    }

    private suspend fun onSaveTvShowWithCategories(remoteTvShow: RemoteTvShowItemDto) {
        localTvDataSource.addTvShowWithCategories(
            tvShow = tvShowRemoteLocalMapper.toLocal(remoteTvShow, listOf(getDeviceLanguage())),
            categories = tvShowGenreIdsRemoteLocalMapper.toLocalList(
                remoteTvShow.genreIds,
                listOf(getDeviceLanguage())
            ),
            storedLanguage = getDeviceLanguage()
        )
    }

    private suspend fun incrementUserInterestByTvShow(remoteCategories: List<RemoteCategoryDto>) {
        remoteCategories.map(RemoteCategoryDto::id)
            .map { localTvDataSource.incrementGenreInterest(it.toLong()) }
    }
}