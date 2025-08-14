package com.amsterdam.repository.utils

import com.amsterdam.repository.dto.remote.UserListDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.UserListItemRemoteDto

val remoteListResponse = UserListDetailsRemoteResponse(
    id = 0,
    name = "",
    description = "",
    itemCount = 0,
    favoriteCount = 0,
    createdBy = "",
    items = emptyList(),
    posterPath = ""
)
val listItem = UserListItemRemoteDto(
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