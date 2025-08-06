package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.TvShowRepository

class GetEpisodeVideosUseCase (
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ) = tvShowRepository.getEpisodeVideoUrl(tvShowId, seasonNumber, episodeNumber)

}