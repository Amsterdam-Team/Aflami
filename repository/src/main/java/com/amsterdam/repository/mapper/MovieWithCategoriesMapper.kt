package com.amsterdam.repository.mapper

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.relation.MovieWithCategories

fun MovieWithCategories.toEntity(): Movie {
    return Movie(
        id = movie.movieId,
        name = movie.name,
        description = movie.description,
        posterUrl = movie.poster,
        releaseDate = movie.releaseDate,
        rating = movie.rating,
        categories = categories
            .distinctBy { it.categoryId }
            .map { it.toEntity() },
        popularity = movie.popularity,
        runTimeInMinutes = movie.movieLength,
        originCountry = movie.originCountry
    )
}