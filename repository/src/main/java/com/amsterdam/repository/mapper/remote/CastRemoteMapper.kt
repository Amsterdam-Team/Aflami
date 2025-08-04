package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Actor
import com.amsterdam.entity.Gender
import com.amsterdam.repository.dto.remote.RemoteCastDto

fun RemoteCastDto.toActorEntity(): Actor =
    Actor(
        id = id,
        name = name,
        imageUrl = fullProfilePath.orEmpty(),
        popularity = popularity,
        gender = if (gender == 2) Gender.Male else Gender.Female
    )


fun List<RemoteCastDto>.toActorEntityList(): List<Actor> = map { it.toActorEntity() }