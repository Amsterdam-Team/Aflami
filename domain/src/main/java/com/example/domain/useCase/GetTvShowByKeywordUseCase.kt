package com.example.domain.useCase

import com.example.domain.repository.TvShowRepository
import com.example.entity.GenreType
import com.example.entity.TvShow

class GetTvShowByKeywordUseCase(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Float = 0f,
        genreType: GenreType = GenreType.ALL
    ): List<TvShow> {
        return tvShowRepository.getTvShowByKeyword(keyword = keyword, rating = rating, genreType = genreType)
            .sortedByDescending { it.popularity }
    }
}