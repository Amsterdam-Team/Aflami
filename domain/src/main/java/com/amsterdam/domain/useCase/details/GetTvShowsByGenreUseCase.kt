package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.TvShowGenre

class GetTvShowsByGenreUseCase(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(
        selectedGenre: TvShowGenre,
        page: Int
    ): List<TvShow> = tvShowRepository.getTvShowsByGenre(selectedGenre, page)
}
