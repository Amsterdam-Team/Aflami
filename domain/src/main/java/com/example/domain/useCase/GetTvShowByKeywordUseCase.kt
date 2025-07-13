package com.example.domain.useCase

import com.example.domain.repository.TvShowRepository
import com.example.domain.useCase.genreTypes.TvShowGenre
import com.example.entity.TvShow

class GetTvShowByKeywordUseCase(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Float = 0f,
        tvShowGenre: TvShowGenre = TvShowGenre.ALL
    ): List<TvShow> {
        return tvShowRepository.getTvShowByKeyword(keyword = keyword, rating = rating, movieGenre = tvShowGenre)
            .sortedByDescending { it.popularity }
    }
}