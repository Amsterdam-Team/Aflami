package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.CharacterApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.CharacterRemoteDataSource
import com.amsterdam.repository.dto.remote.RemoteCharacterItemDto
import com.amsterdam.repository.dto.remote.RemoteCharacterResponse
import javax.inject.Inject

class CharacterRemoteDataSourceImpl @Inject constructor(
    private val characterApiService: CharacterApiService
) : CharacterRemoteDataSource {

    override suspend fun getRandomizedTrendingCharacter(requiredNumber: Int): List<RemoteCharacterItemDto> {
        val totalPages = getTrendingCharacters(FIRST_PAGE).totalPages

        return (FIRST_PAGE..totalPages)
                .shuffled()
                .fold(emptyList()) { accumulatedCharacters, page ->
                    accumulatedCharacters.takeIf { it.size >= requiredNumber }
                    ?: getTrendingCharacters(page).results
                            .filter(::onFilterHighQualityCharacterData)
                            .shuffled()
                            .distinctBy(RemoteCharacterItemDto::id)
                            .plus(accumulatedCharacters)
                            .take(requiredNumber)
                }
    }

    private fun onFilterHighQualityCharacterData(character: RemoteCharacterItemDto): Boolean {
        return character.name.isNotBlank() && !character.profilePath.isNullOrBlank()
    }

    private suspend fun getTrendingCharacters(page: Int): RemoteCharacterResponse {
        return responseCall(
            execute = {
                characterApiService.getTrendingCharacters(page = page)
            }
        )
    }

    private companion object {
        const val FIRST_PAGE = 1
    }
}