package com.example.domain.useCase

import com.example.domain.repository.TvShowRepository
import com.example.entity.TvShow

class GetTopRatedTvShowsUseCase (private val tvShowRepository: TvShowRepository) {
    suspend operator fun invoke(): List<TvShow> {
        return tvShowRepository.getTopRatedTvShows()
    }
}