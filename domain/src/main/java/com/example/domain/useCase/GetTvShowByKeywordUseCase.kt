package com.example.domain.usecase

import com.example.domain.repository.TvShowRepository
import com.example.entity.TvShow

class GetTvShowByKeywordUseCase(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Float = 0f,
        categoryName: String = ""
    ): List<TvShow> {
        return tvShowRepository.getTvShowByKeyword(keyword)
            .filter { it.rating == rating }
            .filter { it.categories.any { category -> category.name == categoryName } }
    }
}