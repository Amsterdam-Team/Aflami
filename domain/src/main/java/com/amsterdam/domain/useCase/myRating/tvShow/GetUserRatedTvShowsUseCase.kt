package com.amsterdam.domain.useCase.myRating.tvShow

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.TvShow
import kotlin.collections.sortedByDescending

class GetUserRatedTvShowsUseCase(
    private val tvShowRepository: TvShowRepository,
) {

    suspend fun getRatedTvShows(): List<UserRatedTvShow> {
        return tvShowRepository.getUserRatedTvShows().sortedByDescending { it.userRate }
    }

    data class UserRatedTvShow(
        val tvShow: TvShow,
        val userRate: Int
    )
}