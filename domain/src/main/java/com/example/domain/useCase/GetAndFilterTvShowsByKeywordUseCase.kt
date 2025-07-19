package com.example.domain.useCase

import com.example.domain.repository.TvShowRepository
import com.example.entity.TvShow
import com.example.entity.category.TvShowGenre
import kotlin.math.floor

class GetAndFilterTvShowsByKeywordUseCase(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Int = 0,
        tvGenre: TvShowGenre = TvShowGenre.ALL
    ): List<TvShow> {
        val userInterest = tvShowRepository.getAllGenreInterests()

        return tvShowRepository.getTvShowByKeyword(keyword = keyword)
            .filterMoviesWithRatingAndGenre(rating, genre = tvGenre)
            .sortedByDescending { movie ->
                movie.categories.maxOfOrNull { userInterest[it] ?: 0 }
            }
    }


    private fun List<TvShow>.filterMoviesWithRatingAndGenre(
        rating: Int,
        genre: TvShowGenre
    ): List<TvShow> {
        return this.filter { item -> floor(item.rating) >= rating }
            .filter { tv ->
                if (genre == TvShowGenre.ALL) return@filter true
                tv.categories.any { it == genre }
            }
    }
}