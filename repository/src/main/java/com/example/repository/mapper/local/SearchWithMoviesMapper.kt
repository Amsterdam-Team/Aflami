package com.example.repository.mapper.local

import com.example.entity.Movie
import com.example.repository.dto.local.relation.SearchWithMovies

class SearchWithMoviesMapper {
    fun mapFromSearchWithMovies(searchWithMovies: SearchWithMovies): List<Movie> {
        return searchWithMovies.movies.map {
            Movie(
                id = it.movieId,
                name = it.name,
                description = it.description,
                poster = it.poster,
                productionYear = it.productionYear,
                rating = it.rating,
                popularity = it.popularity
            )
        }
    }
}