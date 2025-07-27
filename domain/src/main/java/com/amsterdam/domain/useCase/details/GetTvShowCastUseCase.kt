package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.Actor

class GetTvShowCastUseCase(private val tvShowRepository: TvShowRepository) {
    suspend operator fun invoke(tvShowId: Long): List<Actor> {
        return tvShowRepository.getTvShowCast(tvShowId)
            .sortedByDescending { it.popularity }
    }
}