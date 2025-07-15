package com.example.repository.mapper.local

import com.example.entity.Movie
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.relation.MovieWithCategories

class MovieLocalMapper(
    private val categoryLocalMapper: CategoryLocalMapper
) {

    fun mapToMovie(movieWithCategories: MovieWithCategories): Movie {
        return Movie(
            id = movieWithCategories.movie.movieId,
            name = movieWithCategories.movie.name,
            description = movieWithCategories.movie.description,
            poster = movieWithCategories.movie.poster,
            productionYear = movieWithCategories.movie.productionYear,
            rating = movieWithCategories.movie.rating,
            categories = categoryLocalMapper.mapToMovieCategories(movieWithCategories.categories),
            popularity = movieWithCategories.movie.popularity
        )
    }

    fun mapToLocalMovie(movie: Movie): LocalMovieDto {
        return LocalMovieDto(
            movieId = movie.id,
            name = movie.name,
            description = movie.description,
            poster = movie.poster,
            productionYear = movie.productionYear,
            rating = movie.rating,
            popularity = movie.popularity
        )
    }

    fun mapToMovies(moviesWithCategories: List<MovieWithCategories>): List<Movie> {
        return moviesWithCategories.map { mapToMovie(it) }
    }

    fun mapToLocalMovies(movies: List<Movie>): List<LocalMovieDto> {
        return movies.map { mapToLocalMovie(it) }
    }
}
