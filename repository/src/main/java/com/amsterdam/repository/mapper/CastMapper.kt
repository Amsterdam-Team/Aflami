package com.amsterdam.repository.mapper

import com.amsterdam.entity.Actor
import com.amsterdam.entity.Gender
import com.amsterdam.repository.dto.remote.RemoteCastDto

fun RemoteCastDto.toEntity(): Actor =
    Actor(
        id = id,
        name = name,
        imageUrl = fullProfilePath.orEmpty(),
        popularity = popularity,
        gender = if (gender == 2) Gender.Male else Gender.Female
    )


fun List<RemoteCastDto>.toEntityList(): List<Actor> = map { it.toEntity() }