package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.relation.MovieWithCategories

fun MovieWithCategories.toEntity(): Movie =
    Movie(
        id = movie.movieId,
        name = movie.name,
        description = movie.description,
        posterUrl = movie.poster,
        releaseDate = movie.releaseDate,
        rating = movie.rating,
        categories = categories
            .distinctBy { it.categoryId }
            .map { it.toMovieGenreEntity() },
        popularity = movie.popularity,
        runTimeInMinutes = movie.movieLength,
        originCountry = movie.originCountry
    )