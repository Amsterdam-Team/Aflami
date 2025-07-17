package com.example.repository.mapper.local

import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun MovieWithCategories.toDomain(): Movie {
    return movie.toDomain(
        categories = categories.mapNotNull { it.toDomainOrNull() }
    )
}

fun LocalMovieCategoryDto.toDomainOrNull(): MovieGenre? {
    return runCatching { MovieGenre.valueOf(name.uppercase()) }.getOrNull()
}

fun MovieGenre.toLocalDto(): LocalMovieCategoryDto {
    return LocalMovieCategoryDto(
        categoryId = this.ordinal.toLong(),
        name = this.name
    )
}

fun LocalMovieDto.toDomain(categories: List<MovieGenre>): Movie {
    return Movie(
        id = movieId,
        name = name,
        description = description,
        poster = poster,
        productionYear = productionYear,
        categories = categories,
        rating = rating,
        popularity = popularity
    )
}

fun Movie.toLocalMovieDto(): LocalMovieDto {
    return LocalMovieDto(
        movieId = id,
        name = name,
        description = description,
        poster = poster,
        productionYear = productionYear,
        popularity = popularity,
        rating = rating
    )
}

fun Movie.toSearchMovieCrossRefs(
    searchKeyword: String,
    searchType: SearchType
): List<SearchMovieCrossRefDto> {
    return categories.map {
        SearchMovieCrossRefDto(
            movieId = id,
            searchKeyword = searchKeyword,
            searchType = searchType
        )
    }
}

fun Flow<List<MovieWithCategories>>.toMovieListFlow(): Flow<List<Movie>> {
    return this.map { list -> list.map { it.toDomain() } }
}
