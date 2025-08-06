package com.amsterdam.viewmodel.utils.entityHelper

import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.TvShowGenre
import kotlinx.datetime.LocalDate

fun createTvShow(
    id: Long = 1L,
    name: String = "Default Movie",
    description: String = "A sample movie description.",
    poster: String = "https://example.com/poster.jpg",
    videoUrl: String = "https://videxample.com/poster.jpg",
    airDate: Int = 2024,
    genres: List<TvShowGenre> = emptyList(),
    rating: Float = 4.5f,
    originCountry: String = "USA",
    popularity: Double = 0.0,
    seasonCount: Int = 1,
): TvShow {
    return TvShow(
        id = id,
        name = name,
        description = description,
        posterUrl = poster,
        categories = genres,
        rating = rating,
        popularity = popularity,
        originCountry = originCountry,
        airDate = LocalDate(airDate, 1, 1),
        seasonCount = seasonCount,
        videoUrl = videoUrl,
    )
}