package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.TvShowGenre
import kotlin.math.floor

class GetAndFilterTvShowsByKeywordUseCase (
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(
        keyword: String,
        page: Int = 1,
        tvShowsPerPage: Int = 20,
        rating: Int = 0,
        tvGenre: TvShowGenre = TvShowGenre.ALL
    ): List<TvShow> {
        return tvShowRepository.getTvShowByKeyword(keyword = keyword, page, tvShowsPerPage)
            .filterMoviesWithRatingAndGenre(rating, genre = tvGenre)
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