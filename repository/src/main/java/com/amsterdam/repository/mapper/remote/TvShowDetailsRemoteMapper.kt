package com.amsterdam.repository.mapper.remote

import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase.TvShowDetails
import com.amsterdam.entity.TvShow
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.shared.toTvShowGenre
import com.amsterdam.repository.utils.toSafeLocalDate

fun TvShowDetailsRemoteResponse.toTvShowDetailsEntity(): TvShowDetails {
    val tvShow = TvShow(
        id = id,
        name = title,
        description = overview,
        posterUrl = fullPosterPath.orEmpty(),
        airDate = releaseDate.toSafeLocalDate(),
        categories = genres.map { it.id.toLong().toTvShowGenre() },
        rating = voteAverage.toFloat(),
        popularity = popularity,
        seasonCount = seasonCount,
        originCountry = originCountry.firstOrNull() ?: "",
        productionCompanies = productionCompanies.toProductionCompanyEntityList()
    )

    return TvShowDetails(
        tvShow = tvShow,
        actors = credits.cast.toActorEntityList(),
        seasons = seasons.toSeasonEntityList(),
        reviews = reviews.results.toReviewEntityList(),
        similarTvShows = similar.results.toTvShowEntityList(),
        gallery = images.toImageUrlsEntityList(),
        posters = images.toImageUrlsEntityList(),
        productionsCompanies =productionCompanies.toProductionCompanyEntityList(),
    )
}
