package com.example.domain.useCase

import com.example.domain.common.ContentFilteringExtensions.filterByMinRating
import com.example.domain.common.ContentFilteringExtensions.sortByPopularityDescending
import com.example.domain.common.ContentFilteringExtensions.throwIfEmpty
import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.repository.TvShowRepository
import com.example.entity.TvShow
import com.example.entity.category.TvShowCategoryType

class GetTvShowByKeywordUseCase(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Int = 0,
        tvGenre: TvShowCategoryType
    ): List<TvShow> {
        return tvShowRepository.getTvShowByKeyword(keyword = keyword)
            .filterByMinRating(rating)
            .filter { tv ->
                if (tvGenre == TvShowCategoryType.ALL)
                    return@filter true

                tv.categories.any { it == tvGenre }
            }
            .sortByPopularityDescending()
            .throwIfEmpty { NoSearchByKeywordResultFoundException() }
    }
}