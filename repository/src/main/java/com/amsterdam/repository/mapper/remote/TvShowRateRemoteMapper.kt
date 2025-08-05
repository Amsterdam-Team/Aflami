package com.amsterdam.repository.mapper.remote

import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto

fun RemoteTvShowItemDto.toTvShowUserRateEntity(dto: RemoteTvShowItemDto): UserRatedTvShow {
    return UserRatedTvShow(
        tvShow = dto.toEntity(),
        userRate = rating.toInt()
    )
}

fun List<RemoteTvShowItemDto>.toTvShowUserRateEntityList(): List<UserRatedTvShow> = map { it.toTvShowUserRateEntity(it)}
