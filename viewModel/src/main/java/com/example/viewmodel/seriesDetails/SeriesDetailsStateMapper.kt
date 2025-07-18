package com.example.viewmodel.seriesDetails

import com.example.domain.useCase.GetTvShowDetailsUseCase.TvShowDetails
import com.example.entity.Episode
import com.example.entity.Season
import com.example.viewmodel.movieDetails.MovieDetailsUiStateMapper
import com.example.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState
import com.example.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState.EpisodeUiState
import com.example.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras
import com.example.viewmodel.shared.Selectable
import com.example.viewmodel.shared.movieAndSeriseDetails.ActorUiState
import com.example.viewmodel.shared.movieAndSeriseDetails.ProductionCompanyUiState
import com.example.viewmodel.shared.movieAndSeriseDetails.ReviewUiState
import com.example.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState

class SeriesDetailsStateMapper(
    private val movieDetailsStateMapper: MovieDetailsUiStateMapper,
) {
    fun mapToSeriesDetailsUiState(
        tvShowDetails: TvShowDetails,
        seasons: List<Season> = emptyList(),
        isLoading: Boolean = false,
        networkError: Boolean = false,
        isRateDialogVisible: Boolean = false,
        isAddToListDialogVisible: Boolean = false,
    ): SeriesDetailsUiState {
        return SeriesDetailsUiState(
            tvShowId = tvShowDetails.tvShow.id,
            posterUrl = tvShowDetails.tvShow.posterUrl,
            rating = movieDetailsStateMapper.ratingToRatingString(tvShowDetails.tvShow.rating),
            title = tvShowDetails.tvShow.name,
            categories = tvShowDetails.categories,
            airDate = movieDetailsStateMapper.productionYearToDate(tvShowDetails.tvShow.productionYear),
            seasonCount = formatSeasonCount(tvShowDetails.seasons.size),
            originCountry = tvShowDetails.tvShow.originCountry,
            description = tvShowDetails.tvShow.description,
            cast = tvShowDetails.actors.map {
                ActorUiState(
                    photo = it.imageUrl,
                    name = it.name
                )
            },
            isRateDialogVisible = isRateDialogVisible,
            isAddToListDialogVisible = isAddToListDialogVisible,
            extraItem = listOf(
                Selectable(isSelected = true, SeriesExtras.SEASONS),
                Selectable(isSelected = false, SeriesExtras.MORE_LIKE_THIS),
                Selectable(isSelected = false, SeriesExtras.REVIEWS),
                Selectable(isSelected = false, SeriesExtras.GALLERY),
                Selectable(isSelected = false, SeriesExtras.COMPANY_PRODUCTION)
            ),
            seasons = mapToSeasonUiState(seasons),
            similarSeries = tvShowDetails.similarTvShows.map {
                SimilarMovieUiState(
                    rate = movieDetailsStateMapper.ratingToRatingString(it.rating),
                    name = it.name,
                    productionYear = it.productionYear.toString(),
                    posterUrl = it.posterUrl
                )
            },
            reviews = tvShowDetails.reviews.map {
                ReviewUiState(
                    author = it.reviewerName,
                    username = it.reviewerUsername,
                    rating = movieDetailsStateMapper.ratingToRatingString(it.rating),
                    content = it.content,
                    date = movieDetailsStateMapper.dateToString(it.date),
                    imageUrl = it.imageUrl.takeIf { it.isNotBlank() }
                )
            },
            gallery = tvShowDetails.tvShowGallery.map { it },
            productionCompanies = tvShowDetails.productionsCompanies.map { company ->
                ProductionCompanyUiState(
                    image = company.imageUrl,
                    name = company.name,
                    country = company.country
                )
            },
            isLoading = isLoading,
            networkError = networkError,
        )
    }

    fun mapToSeasonUiState(
        seasons: List<Season>,
        episodesBySeason: Map<Int, List<Episode>> = emptyMap()
    ): List<SeasonUiState> {
        return seasons.map { season ->
            val episodes = episodesBySeason[season.seasonNumber] ?: emptyList()
            SeasonUiState(
                id = season.id,
                seasonNumber = season.seasonNumber,
                episodeCount = formatEpisodeCount(episodes.size),
                episodes = mapToEpisodeUiState(episodes)
            )
        }
    }

    fun mapToEpisodeUiState(episodes: List<Episode>): List<EpisodeUiState> {
        return episodes.map { episode ->
            EpisodeUiState(
                id = episode.id,
                number = episode.episodeNumber.toString(),
                title = episode.title,
                rating = movieDetailsStateMapper.ratingToRatingString(episode.rating),
                imageUrl = episode.stillUrl,
                imageNumber = episode.episodeNumber.toString(),
                description = episode.description,
                duration = formatDuration(episode.runtime),
                airDate = movieDetailsStateMapper.dateToString(episode.airDate)
            )
        }
    }

    private fun formatSeasonCount(count: Int) = "$count Season"

    private fun formatEpisodeCount(count: Int): String {
        return if (count == 1) {
            "$count Episode"
        } else {
            "$count Episodes"
        }
    }

    private fun formatDuration(duration: Int): String {
        val hours = duration / 60
        val minutes = duration % 60
        return if (hours > 0) {
            "${hours}h ${minutes}m"
        } else {
            "${minutes}m"
        }
    }
}