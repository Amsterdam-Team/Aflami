package com.amsterdam.remotedatasource.util

import com.amsterdam.repository.dto.remote.UserListDetailsResponse
import com.amsterdam.repository.dto.remote.UserListItemDto

val remoteListResponse = UserListDetailsResponse(
    id = 0,
    name = "",
    description = "",
    itemCount = 0,
    favoriteCount = 0,
    createdBy = "",
    items = emptyList(),
    posterPath = ""
)
val listItem = UserListItemDto(
    id = 0,
    title = "",
    name = "",
    overview = "",
    originalLanguage = "",
    genreIds = emptyList(),
    posterPath = "",
    backdropPath = "",
    mediaType = "",
    releaseDate = "",
    firstAirDate = "",
    popularity = 0.0,
    voteAverage = 0.0,
    adult = false,
    voteCount = 0
)

val listItems = (0..5).map { listItem.copy(id = it.toLong()) }