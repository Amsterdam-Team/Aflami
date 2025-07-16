package com.example.domain.useCase

import com.example.domain.common.ContentFilteringExtensions.filterByCategory
import com.example.domain.common.ContentFilteringExtensions.filterByMinRating
import com.example.domain.common.ContentFilteringExtensions.sortByPopularityDescending
import com.example.domain.common.ContentFilteringExtensions.throwIfEmpty
import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.repository.TvShowRepository
import com.example.entity.TvShow

class GetTvShowByKeywordUseCase(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(
        keyword: String,
        page: Int = 1,
        rating: Int = 0,
        tvShowGenreId: Int = 0
    ): List<TvShow> {
        return tvShowRepository
            .getTvShowByKeyword(keyword = keyword, page = page)
            .filterByMinRating(rating)
            .filterByCategory(tvShowGenreId)
            .sortByPopularityDescending()
            .throwIfEmpty { NoSearchByKeywordResultFoundException() }
    }
}