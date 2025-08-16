package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RemoteCharacterItemDto

interface CharacterRemoteDataSource {
    suspend fun getRandomizedTrendingCharacter(
        requiredNumber: Int,
    ): List<RemoteCharacterItemDto>
}