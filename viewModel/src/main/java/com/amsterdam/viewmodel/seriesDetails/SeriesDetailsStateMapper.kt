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
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState.DurationUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState.EpisodeUiState
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SimilarTvShowUiState
import com.amsterdam.viewmodel.shared.RateDialogUiState
import com.amsterdam.viewmodel.shared.mappers.toFormattedRating
import com.amsterdam.viewmodel.utils.toFormattedString
import com.amsterdam.viewmodel.utils.toShortMonthString

fun List<Episode>.toUiState(currentLanguage: String) = map { it.toUiState(currentLanguage) }

fun TvShowDetails.toUiState(currentLanguage: String): SeriesDetailsUiState {
    return SeriesDetailsUiState(
        videoUrl = tvShow.videoUrl ?: "",
        tvShowId = tvShow.id,
        rating = tvShow.rating.toFormattedRating(),
        posterUrl = tvShow.posterUrl,
        title = tvShow.name,
        airDate = tvShow.airDate.toFormattedString(),
        categories = tvShow.categories,
        seasonCount = seasons.size,
        originCountry = tvShow.originCountry,
        description = tvShow.description,
        cast = actors.toActorsUiState(),
        isRateDialogVisible = false,
        isAddToListDialogVisible = false,
        extraItem = SeriesDetailsUiState.defaultSeriesExtrasItems,
        seasons = seasons.toSeasonUiState(currentLanguage = currentLanguage),
        similarSeries = similarTvShows.toSimilarTvShowUiStates(),
        reviews = reviews.toReviewTvShowUiStates(),
        gallery = gallery,
        postersUrls = posters,
        productionCompanies = productionsCompanies.toProductionTvShowCompanyUiStates(),
        rateDialogUiState = RateDialogUiState(selectedStarIndex = userRate),
        currentLanguage = currentLanguage
    )
}

private fun List<TvShow>.toSimilarTvShowUiStates(): List<SimilarTvShowUiState> {
    return this.map { it.toSimilarTvShowUiState() }
}

private fun List<Review>.toReviewTvShowUiStates(): List<ReviewTvShowUiState> {
    return this.map { it.toReviewTvShowUiState() }
}

private fun List<Actor>.toActorsUiState(): List<ActorTvShowUiState> = map { it.toActorUiState() }

private fun List<ProductionCompany>.toProductionTvShowCompanyUiStates(): List<ProductionTvShowCompanyUiState> {
    return this.map { it.toProductionTvShowCompanyUiState() }
}

private fun TvShow.toSimilarTvShowUiState(): SimilarTvShowUiState {
    return SimilarTvShowUiState(
        movieId = id,
        rate = rating.toFormattedRating(),
        name = name,
        productionYear = airDate?.year?.toString() ?: "",
        posterUrl = posterUrl,
        isAdult = isAdult
    )
}


private fun List<Season>.toSeasonUiState(
    episodesBySeason: Map<Int, List<Episode>> = emptyMap(), currentLanguage: String
): List<SeasonUiState> {
    return map { it.toUiState(episodesBySeason[it.seasonNumber].orEmpty(), currentLanguage) }
}

private fun Season.toUiState(episodes: List<Episode>, currentLanguage: String): SeasonUiState {
    return SeasonUiState(
        id = id,
        seasonNumber = seasonNumber,
        title = title,
        episodeCount = episodeCount,
        episodes = episodes.map { it.toUiState(currentLanguage) }
    )
}

private fun Episode.toUiState(currentLanguage: String): EpisodeUiState {
    return EpisodeUiState(
        id = id,
        number = episodeNumber,
        title = title,
        rating = rating.toFormattedRating(),
        imageUrl = episodeImageUrl,
        imageNumber = episodeNumber,
        description = description,
        duration = runTimeInMinutes.toDurationUiState(),
        airDate = airDate.toShortMonthString(language = currentLanguage)
    )
}

fun Int.toDurationUiState(): DurationUiState {
    return DurationUiState(
        hour = this / 60,
        minute = this % 60
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

private fun Actor.toActorUiState(): ActorTvShowUiState =
    ActorTvShowUiState(photo = imageUrl, name = name)

private fun ProductionCompany.toProductionTvShowCompanyUiState(): ProductionTvShowCompanyUiState {
    return ProductionTvShowCompanyUiState(
        image = imageUrl,
        name = name,
        country = country
    )
}