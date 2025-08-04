package com.amsterdam.repository.mapper.remote

import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase.TvShowDetails
import com.amsterdam.entity.TvShow
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.shared.toTvShowGenre
import com.amsterdam.repository.utils.toSafeLocalDate

fun TvShowDetailsRemoteResponse.toEntity(): TvShowDetails {
    val tvShow = TvShow(
        id = id,
        name = title,
        description = overview,
        posterUrl = fullPosterPath.orEmpty(),
        airDate = releaseDate.toSafeLocalDate(),
        categories = genres.map { toTvShowGenre(it.id.toLong()) },
        rating = voteAverage.toFloat(),
        popularity = popularity,
        seasonCount = seasonCount,
        originCountry = originCountry.firstOrNull() ?: "",
        videoUrl = videos.results.firstOrNull()?.fullVideoUrl ?: ""
    )

    return TvShowDetails(
        tvShow = tvShow,
        actors = credits.cast.toEntityList(),
        seasons = seasons.toEntityList(),
        reviews = reviews.results.toEntityList(),
        similarTvShows = similar.results.toEntityList(),
        gallery = images.toEntityList(),
        posters = images.toEntityList(),
        productionsCompanies =productionCompanies.toEntityList(),
    )
}
