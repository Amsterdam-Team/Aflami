/*
package com.example.repository.mapper.local.testFactory

import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.relation.MovieWithCategories

fun createLocalMovieDtotest(
    movieId: Long = 101,
    name: String = "Inception",
    description: String = "A mind-bending thriller",
    poster: String = "poster_url.jpg",
    productionYear: Int = 2010,
    rating: Float = 8.8f,
    popularity: Double = 99.5,
    movieLength: Int = 148,
    originCountry: String = "USA",
    hasVideo: Boolean = true
): LocalMovieDto {
    return LocalMovieDto(
        movieId = movieId,
        name = name,
        description = description,
        poster = poster,
        productionYear = productionYear,
        rating = rating,
        popularity = popularity,
        movieLength = movieLength,
        originCountry = originCountry,
        hasVideo = hasVideo
    )
}

fun createMovie(
    id: Long = 202,
    name: String = "Interstellar",
    description: String = "Exploration beyond stars",
    posterUrl: String = "interstellar.jpg",
    productionYear: UInt = 2014u,
    rating: Float = 9.0f,
    popularity: Double = 95.2,
    runTime: Int = 169,
    originCountry: String = "USA",
    hasVideo: Boolean = false,
    categories: List<MovieGenre> = emptyList()
): Movie {
    return Movie(
        id = id,
        name = name,
        description = description,
        posterUrl = posterUrl,
        productionYear = productionYear,
        rating = rating,
        popularity = popularity,
        runTime = runTime,
        originCountry = originCountry,
        hasVideo = hasVideo,
        categories = categories
    )
}

fun createMovieWithCategories(
    dto: LocalMovieDto = createLocalMovieDtotest(),
    categories: List<LocalMovieCategoryDto> = listOf(createLocalMovieCategoryDto())
): MovieWithCategories {
    return MovieWithCategories(movie = dto, categories = categories)
}*/
