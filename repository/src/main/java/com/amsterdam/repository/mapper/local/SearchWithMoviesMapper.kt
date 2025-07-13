package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.relation.SearchWithMovies

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
                categories = emptyList(), //todo
                popularity = it.popularity
            )
        }
    }
}