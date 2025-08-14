package com.amsterdam.repository.mapper

import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.repository.dto.remote.TvShowItemRemoteDto

fun TvShowItemRemoteDto.toTvShowUserRateEntity(dto: TvShowItemRemoteDto): UserRatedTvShow {
    return UserRatedTvShow(
        tvShow = dto.toEntity(),
        userRate = rating.toInt()
    )
}

fun List<TvShowItemRemoteDto>.toTvShowUserRateEntityList(): List<UserRatedTvShow> = map { it.toTvShowUserRateEntity(it)}
