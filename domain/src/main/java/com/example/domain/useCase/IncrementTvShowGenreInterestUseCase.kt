package com.example.domain.useCase

import com.example.domain.repository.TvShowRepository
import com.example.entity.category.TvShowGenre

class IncrementTvShowGenreInterestUseCase(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(genre: TvShowGenre) {
        tvShowRepository.incrementGenreInterest(genre)
    }
}