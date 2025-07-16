package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.entity.Movie
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.relation.MovieWithCategories

class MovieWithCategoryMapper {

    fun mapToMovie(movieWithCategories: MovieWithCategories): Movie {
        return Movie(
            id = movieWithCategories.movie.movieId,
            name = movieWithCategories.movie.name,
            description = movieWithCategories.movie.description,
            poster = movieWithCategories.movie.poster,
            productionYear = movieWithCategories.movie.productionYear,
            rating = movieWithCategories.movie.rating,
            categories = movieWithCategories.categories.map { mapToCategory(it) },
            popularity = movieWithCategories.movie.popularity,
            movieLength = movieWithCategories.movie.movieLength,
            originCountry = movieWithCategories.movie.originCountry,
            hasVideo = movieWithCategories.movie.hasVideo
        )
    }

    private fun mapToCategory(localMovieCategory: LocalMovieCategoryDto): Category {
        return Category(
            id = localMovieCategory.categoryId,
            name = localMovieCategory.name,
            image = ""
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
            popularity = movie.popularity,
            movieLength = movie.movieLength,
            originCountry = movie.originCountry,
            hasVideo = movie.hasVideo
        )
    }
}