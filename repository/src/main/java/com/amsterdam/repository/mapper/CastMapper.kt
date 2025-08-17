package com.amsterdam.repository.mapper

import com.amsterdam.entity.Actor
import com.amsterdam.entity.Gender
import com.amsterdam.repository.dto.remote.CastRemoteDto

fun CastRemoteDto.toEntity(): Actor = Actor(
    id = id,
    name = name,
    imageUrl = fullProfilePath.orEmpty(),
    popularity = popularity,
    gender = if (gender == 2) Gender.Male else Gender.Female
)

fun List<CastRemoteDto>.toEntityList(): List<Actor> = map { it.toEntity() }