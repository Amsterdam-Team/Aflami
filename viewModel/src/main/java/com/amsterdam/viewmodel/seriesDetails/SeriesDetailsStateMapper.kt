package com.amsterdam.viewmodel.seriesDetails

import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase.TvShowDetails
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Episode
import com.amsterdam.entity.ProductionCompany
import com.amsterdam.entity.Review
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.ActorTvShowUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.ProductionTvShowCompanyUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.ReviewTvShowUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState.EpisodeUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SimilarTvShowUiState
import com.amsterdam.viewmodel.utils.formatDuration
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.utils.toFormattedString
import kotlin.collections.map

fun TvShowDetails.toUiState(): SeriesDetailsUiState {
    return SeriesDetailsUiState(
        videoUrl = tvShow.videoUrl,
        tvShowId = tvShow.id,
        rating = tvShow.rating.toFormattedRating(),
        posterUrl = tvShow.posterUrl,
        title = tvShow.name,
        airDate = tvShow.airDate.toFormattedString(),
        categories = tvShow.categories,
        seasonCount = formatSeasonCount(seasons.size),
        originCountry = tvShow.originCountry,
        description = tvShow.description,
        cast = actors.toActorsUiState(),
        isRateDialogVisible = false,
        isAddToListDialogVisible = false,
        extraItem = SeriesDetailsUiState.defaultSeriesExtrasItems,
        seasons = seasons.toSeasonUiState(),
        similarSeries = similarTvShows.toSimilarTvShowUiStates(),
        reviews = reviews.toReviewTvShowUiStates(),
        gallery = gallery,
        postersUrls = posters,
        productionCompanies = productionsCompanies.toProductionTvShowCompanyUiStates(),
    )
}

private fun TvShow.toSimilarTvShowUiState(): SimilarTvShowUiState {
    return SimilarTvShowUiState(
        movieId = id,
        rate = rating.toFormattedRating(),
        name = name,
        productionYear = airDate.year.toString(),
        posterUrl = posterUrl
    )
}
fun List<TvShow>.toSimilarTvShowUiStates(): List<SimilarTvShowUiState> {
    return this.map { it.toSimilarTvShowUiState() }
}

private fun List<Season>.toSeasonUiState(
    episodesBySeason: Map<Int, List<Episode>> = emptyMap()
): List<SeasonUiState> {
    return map { it.toUiState(episodesBySeason[it.seasonNumber].orEmpty()) }
}

private fun Season.toUiState(episodes: List<Episode>): SeasonUiState {
    return SeasonUiState(
        id = id,
        seasonNumber = seasonNumber,
        title = title,
        episodeCount = episodeCount,
        episodes = episodes.map(Episode::toUiState)
    )
}

fun List<Episode>.toUiState() = map(Episode::toUiState)

private fun Episode.toUiState(): EpisodeUiState {
    return EpisodeUiState(
        id = id,
        number = episodeNumber,
        title = title,
        rating = rating.toFormattedRating(),
        imageUrl = episodeImageUrl,
        imageNumber = episodeNumber,
        description = description,
        duration = formatDuration(runTimeInMinutes),
        airDate = airDate.toFormattedString()
    )
}


fun Review.toReviewTvShowUiState(): ReviewTvShowUiState {
    return ReviewTvShowUiState(
        author = reviewerName,
        username = reviewerUsername,
        rating = rating.toFormattedRating(),
        content = content,
        date = date.toFormattedString(),
        imageUrl = imageUrl.takeIf { it.isNotBlank() }
    )
}
fun List<Review>.toReviewTvShowUiStates(): List<ReviewTvShowUiState> {
    return this.map { it.toReviewTvShowUiState() }
}

fun Actor.toActorUiState(): ActorTvShowUiState = ActorTvShowUiState(photo = imageUrl, name = name)

fun List<Actor>.toActorsUiState() : List<ActorTvShowUiState> = map { it.toActorUiState() }

private fun formatSeasonCount(count: Int) = "$count Season"

fun ProductionCompany.toProductionTvShowCompanyUiState(): ProductionTvShowCompanyUiState {
    return ProductionTvShowCompanyUiState(
        image = imageUrl,
        name = name,
        country = country
    )
}

fun List<ProductionCompany>.toProductionTvShowCompanyUiStates(): List<ProductionTvShowCompanyUiState> {
    return this.map { it.toProductionTvShowCompanyUiState() }
}

