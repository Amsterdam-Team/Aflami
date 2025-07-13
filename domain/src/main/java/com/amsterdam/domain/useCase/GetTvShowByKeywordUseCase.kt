package com.amsterdam.domain.useCase

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.TvShow

class GetTvShowByKeywordUseCase(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Float = 0f,
        categoryName: String = ""
    ): List<TvShow> {
        return tvShowRepository.getTvShowByKeyword(keyword)
            .sortedByDescending { it.popularity }
//            .filter { it.rating == rating }
//            .filter { it.categories.any { category -> category.name == categoryName } }
    }
}