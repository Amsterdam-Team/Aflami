package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Category
import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories

class MovieWithCategoryMapper {

    fun mapFromLocal(movieWithCategories: MovieWithCategories): Movie {
        return Movie(
            id = movieWithCategories.movie.movieId,
            name = movieWithCategories.movie.name,
            description = movieWithCategories.movie.description,
            poster = movieWithCategories.movie.poster,
            productionYear = movieWithCategories.movie.productionYear,
            rating = movieWithCategories.movie.rating,
            categories = movieWithCategories.categories.map { mapCategoryFromLocal(it) },
            popularity = movieWithCategories.movie.popularity
        )
    }

    private fun mapCategoryFromLocal(localCategory: LocalMovieCategoryDto): Category {
        return Category(
            id = localCategory.categoryId,
            name = localCategory.name,
            image = ""
        )
    }

    fun mapToLocal(movie: Movie): LocalMovieDto {
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
}