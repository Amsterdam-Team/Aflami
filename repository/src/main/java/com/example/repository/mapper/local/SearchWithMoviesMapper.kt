package com.example.repository.mapper.local

import com.example.entity.Movie
import com.example.repository.dto.local.relation.SearchWithMovies

class SearchWithMoviesMapper {
    fun mapToMovies(searchWithMovies: SearchWithMovies): List<Movie> {
        return searchWithMovies.movies.map {
            Movie(
                id = it.movieId,
                name = it.name,
                description = it.description,
                poster = it.poster,
                productionYear = it.productionYear,
                rating = it.rating,
                categories = emptyList(),
                popularity = it.popularity,
                runTime = it.movieLength,
                originCountry = it.originCountry,
                hasVideo = it.hasVideo
            )
        }
    }
}