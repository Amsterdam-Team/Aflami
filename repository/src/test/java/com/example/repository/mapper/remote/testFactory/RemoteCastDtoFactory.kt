package com.example.repository.mapper.remote.testFactory

import com.example.repository.dto.remote.RemoteCastDto

fun createRemoteCastDto(
    id: Long = 1,
    name: String = "Default Name",
    originalName: String = "Default Original Name",
    gender: Int = 1,
    profilePath: String? = "/default.jpg",
    popularity: Double = 0.0,
    knownForDepartment: String = "Acting",
    adult: Boolean = false,
    castId: Int = 0,
    character: String = "Character",
    creditId: String = "credit_123",
    order: Int = 0
): RemoteCastDto {
    return RemoteCastDto(
        adult = adult,
        gender = gender,
        id = id,
        knownForDepartment = knownForDepartment,
        name = name,
        originalName = originalName,
        popularity = popularity,
        profilePath = profilePath,
        castId = castId,
        character = character,
        creditId = creditId,
        order = order
    )
}