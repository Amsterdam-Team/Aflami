package com.example.domain.useCase

import com.example.domain.repository.TvShowRepository
import com.example.entity.TvShow
import com.example.entity.category.TvShowGenre
import kotlin.math.roundToInt

class GetAndFilterTvShowsByKeywordUseCase(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Int = 0,
        tvGenre: TvShowGenre = TvShowGenre.ALL
    ): List<TvShow> {
        return tvShowRepository.getTvShowByKeyword(keyword = keyword)
            .filter { item -> item.rating.roundToInt() >= rating }
            .filter { tv ->
                if (tvGenre == TvShowGenre.ALL) return@filter true
                tv.categories.any { it == tvGenre }
            }
    }
}