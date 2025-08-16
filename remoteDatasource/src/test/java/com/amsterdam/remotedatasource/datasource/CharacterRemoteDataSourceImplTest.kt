package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.CharacterApiService
import com.amsterdam.repository.dto.remote.RemoteCharacterItemDto
import com.amsterdam.repository.dto.remote.RemoteCharacterResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CharacterRemoteDataSourceImplTest {

    private val characterApiService: CharacterApiService = mockk()
    private val characterRemoteDataSourceImpl: CharacterRemoteDataSourceImpl =
        CharacterRemoteDataSourceImpl(characterApiService)

    @Test
    fun `getTrendingCharacter should rethrow a NetworkException when the service provider throws one`() =
        runTest {
            coEvery { characterApiService.getTrendingCharacters(any()) } throws NetworkException()

            assertThrows<NetworkException> {
                characterRemoteDataSourceImpl.getRandomizedTrendingCharacter(
                    requiredNumber = 5
                )
            }
        }

    @Test
    fun `getRandomizedTrendingCharacter should return the required number of character`() = runTest {
        coEvery { characterApiService.getTrendingCharacters(page = 1) } returns firstPageForRandomizedTest
        coEvery { characterApiService.getTrendingCharacters(page = 2) } returns secondPageForRandomizedTest

        val character = characterRemoteDataSourceImpl.getRandomizedTrendingCharacter(3)

        assertThat(character.size).isEqualTo(3)
    }

    @Test
    fun `getRandomizedTrendingCharacter should call the API multiple times (at least twice) to get enough character`() =
        runTest {
            coEvery { characterApiService.getTrendingCharacters(page = 1) } returns firstPageForRandomizedTest
            coEvery { characterApiService.getTrendingCharacters(page = 2) } returns secondPageForRandomizedTestWithSinglePerson

            characterRemoteDataSourceImpl.getRandomizedTrendingCharacter(3)

            coVerify(atLeast = 2) { characterApiService.getTrendingCharacters(page = any()) }
        }

    @Test
    fun `getRandomizedTrendingCharacter should filter out character with incomplete data`() = runTest {
        coEvery { characterApiService.getTrendingCharacters(any()) } returns responseWithDuplicatesAndIncompleteData

        val character = characterRemoteDataSourceImpl.getRandomizedTrendingCharacter(2)

        assertThat(character.size).isEqualTo(2)
        assertThat(character.any { it.name.isBlank() || it.profilePath.isNullOrBlank() }).isFalse()
    }

    @Test
    fun `getRandomizedTrendingCharacter should not return more than the required number of character`() =
        runTest {
            coEvery { characterApiService.getTrendingCharacters(any()) } returns responseWithExtraData

            val character = characterRemoteDataSourceImpl.getRandomizedTrendingCharacter(2)

            assertThat(character.size).isEqualTo(2)
        }

    @Test
    fun `getRandomizedTrendingCharacter should handle duplicate character from different pages`() =
        runTest {
            coEvery { characterApiService.getTrendingCharacters(page = 1) } returns firstPageWithDuplicateId
            coEvery { characterApiService.getTrendingCharacters(page = 2) } returns secondPageWithDuplicateId

            val character = characterRemoteDataSourceImpl.getRandomizedTrendingCharacter(2)

            assertThat(character.size).isEqualTo(2)
            assertThat(character.map { it.id }).containsNoDuplicates()
        }

    @Test
    fun `getRandomizedTrendingCharacter should return an empty list when no high-quality character are found`() =
        runTest {
            coEvery { characterApiService.getTrendingCharacters(any()) } returns responseWithIncompleteData

            val character = characterRemoteDataSourceImpl.getRandomizedTrendingCharacter(2)

            assertThat(character).isEmpty()
        }


    private fun createCharacterItemDto(
        id: Int,
        name: String,
        profilePath: String?,
    ) = RemoteCharacterItemDto(
        id = id,
        name = name,
        profilePath = profilePath,
        adult = false,
        gender = 1,
        popularity = 5.0,
        mediaType = "person",
        originalName = name,
        knownForDepartment = "Acting"
    )

    private fun createMockCharacterResponse(
        totalPages: Int,
        results: List<RemoteCharacterItemDto>
    ): RemoteCharacterResponse {
        return mockk {
            coEvery { this@mockk.totalPages } returns totalPages
            coEvery { this@mockk.results } returns results
        }
    }

    private val characterForRandomizedTest = listOf(
        createCharacterItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createCharacterItemDto(id = 2, name = "Jerry", profilePath = "path2")
    )

    private val characterWithDuplicatesAndIncompleteData = listOf(
        createCharacterItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createCharacterItemDto(id = 2, name = "", profilePath = "path2"),
        createCharacterItemDto(id = 3, name = "Jerry", profilePath = null),
        createCharacterItemDto(id = 4, name = "Spike", profilePath = "path3")
    )

    private val characterWithExtraData = listOf(
        createCharacterItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createCharacterItemDto(id = 2, name = "Jerry", profilePath = "path2"),
        createCharacterItemDto(id = 3, name = "Spike", profilePath = "path3")
    )

    private val characterWithDuplicateId = listOf(
        createCharacterItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createCharacterItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createCharacterItemDto(id = 2, name = "Jerry", profilePath = "path2")
    )

    private val characterWithIncompleteData = listOf(
        createCharacterItemDto(id = 1, name = "", profilePath = "path1"),
        createCharacterItemDto(id = 2, name = "Jerry", profilePath = null)
    )

    private val firstPageForRandomizedTest =
        createMockCharacterResponse(totalPages = 2, results = characterForRandomizedTest)
    private val secondPageForRandomizedTest = createMockCharacterResponse(
        totalPages = 2, results = listOf(
            createCharacterItemDto(id = 3, name = "Spike", profilePath = "path3"),
            createCharacterItemDto(id = 4, name = "Tyke", profilePath = "path4")
        )
    )
    private val secondPageForRandomizedTestWithSinglePerson = createMockCharacterResponse(
        totalPages = 2, results = listOf(
            createCharacterItemDto(id = 3, name = "Spike", profilePath = "path3")
        )
    )
    private val responseWithDuplicatesAndIncompleteData = createMockCharacterResponse(
        totalPages = 1, results = characterWithDuplicatesAndIncompleteData
    )
    private val responseWithExtraData = createMockCharacterResponse(
        totalPages = 1, results = characterWithExtraData
    )
    private val firstPageWithDuplicateId = createMockCharacterResponse(
        totalPages = 2,
        results = listOf(createCharacterItemDto(id = 1, name = "Tom", profilePath = "path1"))
    )
    private val secondPageWithDuplicateId = createMockCharacterResponse(
        totalPages = 2, results = characterWithDuplicateId
    )
    private val responseWithIncompleteData = createMockCharacterResponse(
        totalPages = 1, results = characterWithIncompleteData
    )
}