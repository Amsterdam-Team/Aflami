package com.amsterdam.viewmodel.seriesDetails

import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase.TvShowDetails
import com.amsterdam.entity.Episode
import com.amsterdam.entity.Season
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiStateMapper
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState.EpisodeUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras
import com.amsterdam.viewmodel.shared.Selectable
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ActorUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ProductionCompanyUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ReviewUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState
import javax.inject.Inject

class SeriesDetailsStateMapper @Inject constructor(
    private val movieDetailsStateMapper: MovieDetailsUiStateMapper
){

    fun toUiState(seriesDetails: TvShowDetails): SeriesDetailsUiState = with(seriesDetails) {
        SeriesDetailsUiState(
            tvShowId = tvShow.id,
            rating = movieDetailsStateMapper.ratingToRatingString(tvShow.rating),
            posterUrl = tvShow.posterUrl,
            title = tvShow.name,
            airDate = tvShow.airDate.toString(),
            seasonCount = formatSeasonCount(seasons.size),
            originCountry = tvShow.originCountry,
            description = tvShow.description,
            cast = actors.map {
                ActorUiState(
                    photo = it.imageUrl,
                    name = it.name
                )
            },
            isRateDialogVisible = false,
            isAddToListDialogVisible = false,
            extraItem = listOf(
                Selectable(isSelected = true, SeriesExtras.SEASONS),
                Selectable(isSelected = false, SeriesExtras.MORE_LIKE_THIS),
                Selectable(isSelected = false, SeriesExtras.REVIEWS),
                Selectable(isSelected = false, SeriesExtras.GALLERY),
                Selectable(isSelected = false, SeriesExtras.COMPANY_PRODUCTION)
            ),
            seasons = mapToSeasonUiState(seasons),
            similarSeries = similarTvShows.map {
                SimilarMovieUiState(
                    rate = movieDetailsStateMapper.ratingToRatingString(it.rating),
                    name = it.name,
                    productionYear = it.airDate.year.toString(),
                    posterUrl = it.posterUrl
                )
            },
            reviews = reviews.map {
                ReviewUiState(
                    author = it.reviewerName,
                    username = it.reviewerUsername,
                    rating = movieDetailsStateMapper.ratingToRatingString(it.rating),
                    content = it.content,
                    date = movieDetailsStateMapper.dateToString(it.date),
                    imageUrl = it.imageUrl.takeIf { it.isNotBlank() }
                )
            },
            gallery = gallery.map { it },
            productionCompanies = productionsCompanies.map { company ->
                ProductionCompanyUiState(
                    image = company.imageUrl,
                    name = company.name,
                    country = company.country
                )
            },
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
                title = season.title,
                episodeCount = season.episodeCount,
                episodes = mapToEpisodeUiState(episodes)
            )
        }
    }

    fun mapToEpisodeUiState(episodes: List<Episode>): List<EpisodeUiState> {
        return episodes.map { episode ->
            EpisodeUiState(
                id = episode.id,
                number = episode.episodeNumber,
                title = episode.title,
                rating = movieDetailsStateMapper.ratingToRatingString(episode.rating),
                imageUrl = episode.episodeImageUrl,
                imageNumber = episode.episodeNumber,
                description = episode.description,
                duration = formatDuration(episode.runTimeInMinutes),
                airDate = movieDetailsStateMapper.dateToString(episode.airDate)
            )
        }
    }

    private fun formatSeasonCount(count: Int) = "$count Season"

    private fun formatDuration(duration: Int): String {
        val hours = duration / 60
        val minutes = duration % 60

        return when {
            hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
            hours > 0 -> "${hours}h"
            else -> "${minutes}m"
        }
    }
}