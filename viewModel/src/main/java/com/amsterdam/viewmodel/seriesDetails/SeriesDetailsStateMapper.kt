package com.amsterdam.viewmodel.seriesDetails

import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase.TvShowDetails
import com.amsterdam.entity.Episode
import com.amsterdam.entity.Season
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState.EpisodeUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras
import com.amsterdam.viewmodel.shared.RateDialogUiState
import com.amsterdam.viewmodel.shared.Selectable
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ActorUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ProductionCompanyUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ReviewUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.SimilarMovieUiState
import com.amsterdam.viewmodel.utils.dateToString
import com.amsterdam.viewmodel.utils.formatDuration
import com.amsterdam.viewmodel.utils.ratingToRatingString
import javax.inject.Inject

class SeriesDetailsStateMapper @Inject constructor(
) {

    fun toUiState(seriesDetails: TvShowDetails): SeriesDetailsUiState = with(seriesDetails) {
        SeriesDetailsUiState(
            videoUrl =seriesDetails.tvShow.videoUrl,
            tvShowId = tvShow.id,
            rating = ratingToRatingString(tvShow.rating),
            posterUrl = tvShow.posterUrl,
            title = tvShow.name,
            airDate = dateToString(tvShow.airDate),
            categories = tvShow.categories,
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
                    movieId = it.id,
                    rate = ratingToRatingString(it.rating),
                    name = it.name,
                    productionYear = it.airDate.year.toString(),
                    posterUrl = it.posterUrl
                )
            },
            reviews = reviews.map {
                ReviewUiState(
                    author = it.reviewerName,
                    username = it.reviewerUsername,
                    rating = ratingToRatingString(it.rating),
                    content = it.content,
                    date = dateToString(it.date),
                    imageUrl = it.imageUrl.takeIf { it.isNotBlank() }
                )
            },
            gallery = gallery.map { it },
            postersUrls = posters,
            productionCompanies = productionsCompanies.map { company ->
                ProductionCompanyUiState(
                    image = company.imageUrl,
                    name = company.name,
                    country = company.country
                )
            },
            rateDialogUiState = RateDialogUiState(
                selectedStarIndex = userRate,
                previousStarIndex = userRate,
            )
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
                rating = ratingToRatingString(episode.rating),
                imageUrl = episode.episodeImageUrl,
                imageNumber = episode.episodeNumber,
                description = episode.description,
                duration = formatDuration(episode.runTimeInMinutes),
                airDate = dateToString(episode.airDate),
                videoUrl = episode.videoUrl
            )
        }
    }

    private fun formatSeasonCount(count: Int) = "$count Season"
}