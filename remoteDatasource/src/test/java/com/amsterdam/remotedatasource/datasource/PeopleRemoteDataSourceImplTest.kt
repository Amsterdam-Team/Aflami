package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.PeopleApiService
import com.amsterdam.repository.dto.remote.RemotePeopleItemDto
import com.amsterdam.repository.dto.remote.RemotePeopleResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PeopleRemoteDataSourceImplTest {

    private val peopleApiService: PeopleApiService = mockk()
    private val peopleRemoteDataSourceImpl: PeopleRemoteDataSourceImpl =
        PeopleRemoteDataSourceImpl(peopleApiService)

    @Test
    fun `getTrendingPeople should rethrow a NetworkException when the service provider throws one`() =
        runTest {
            coEvery { peopleApiService.getTrendingPeople(any()) } throws NetworkException()

            assertThrows<NetworkException> {
                peopleRemoteDataSourceImpl.getRandomizedTrendingPeople(
                    requiredNumber = 5
                )
            }
        }

    @Test
    fun `getRandomizedTrendingPeople should return the required number of people`() = runTest {
        coEvery { peopleApiService.getTrendingPeople(page = 1) } returns firstPageForRandomizedTest
        coEvery { peopleApiService.getTrendingPeople(page = 2) } returns secondPageForRandomizedTest

        val people = peopleRemoteDataSourceImpl.getRandomizedTrendingPeople(3)

        assertThat(people.size).isEqualTo(3)
    }

    @Test
    fun `getRandomizedTrendingPeople should call the API multiple times (at least twice) to get enough people`() =
        runTest {
            coEvery { peopleApiService.getTrendingPeople(page = 1) } returns firstPageForRandomizedTest
            coEvery { peopleApiService.getTrendingPeople(page = 2) } returns secondPageForRandomizedTestWithSinglePerson

            peopleRemoteDataSourceImpl.getRandomizedTrendingPeople(3)

            coVerify(atLeast = 2) { peopleApiService.getTrendingPeople(page = any()) }
        }

    @Test
    fun `getRandomizedTrendingPeople should filter out people with incomplete data`() = runTest {
        coEvery { peopleApiService.getTrendingPeople(any()) } returns responseWithDuplicatesAndIncompleteData

        val people = peopleRemoteDataSourceImpl.getRandomizedTrendingPeople(2)

        assertThat(people.size).isEqualTo(2)
        assertThat(people.any { it.name.isBlank() || it.profilePath.isNullOrBlank() }).isFalse()
    }

    @Test
    fun `getRandomizedTrendingPeople should not return more than the required number of people`() =
        runTest {
            coEvery { peopleApiService.getTrendingPeople(any()) } returns responseWithExtraData

            val people = peopleRemoteDataSourceImpl.getRandomizedTrendingPeople(2)

            assertThat(people.size).isEqualTo(2)
        }

    @Test
    fun `getRandomizedTrendingPeople should handle duplicate people from different pages`() =
        runTest {
            coEvery { peopleApiService.getTrendingPeople(page = 1) } returns firstPageWithDuplicateId
            coEvery { peopleApiService.getTrendingPeople(page = 2) } returns secondPageWithDuplicateId

            val people = peopleRemoteDataSourceImpl.getRandomizedTrendingPeople(2)

            assertThat(people.size).isEqualTo(2)
            assertThat(people.map { it.id }).containsNoDuplicates()
        }

    @Test
    fun `getRandomizedTrendingPeople should return an empty list when no high-quality people are found`() =
        runTest {
            coEvery { peopleApiService.getTrendingPeople(any()) } returns responseWithIncompleteData

            val people = peopleRemoteDataSourceImpl.getRandomizedTrendingPeople(2)

            assertThat(people).isEmpty()
        }


    private fun createPeopleItemDto(
        id: Int,
        name: String,
        profilePath: String?,
    ) = RemotePeopleItemDto(
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

    private fun createMockPeopleResponse(
        totalPages: Int,
        results: List<RemotePeopleItemDto>
    ): RemotePeopleResponse {
        return mockk {
            coEvery { this@mockk.totalPages } returns totalPages
            coEvery { this@mockk.results } returns results
        }
    }

    private val peopleForRandomizedTest = listOf(
        createPeopleItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createPeopleItemDto(id = 2, name = "Jerry", profilePath = "path2")
    )

    private val peopleWithDuplicatesAndIncompleteData = listOf(
        createPeopleItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createPeopleItemDto(id = 2, name = "", profilePath = "path2"),
        createPeopleItemDto(id = 3, name = "Jerry", profilePath = null),
        createPeopleItemDto(id = 4, name = "Spike", profilePath = "path3")
    )

    private val peopleWithExtraData = listOf(
        createPeopleItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createPeopleItemDto(id = 2, name = "Jerry", profilePath = "path2"),
        createPeopleItemDto(id = 3, name = "Spike", profilePath = "path3")
    )

    private val peopleWithDuplicateId = listOf(
        createPeopleItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createPeopleItemDto(id = 1, name = "Tom", profilePath = "path1"),
        createPeopleItemDto(id = 2, name = "Jerry", profilePath = "path2")
    )

    private val peopleWithIncompleteData = listOf(
        createPeopleItemDto(id = 1, name = "", profilePath = "path1"),
        createPeopleItemDto(id = 2, name = "Jerry", profilePath = null)
    )

    private val firstPageForRandomizedTest =
        createMockPeopleResponse(totalPages = 2, results = peopleForRandomizedTest)
    private val secondPageForRandomizedTest = createMockPeopleResponse(
        totalPages = 2, results = listOf(
            createPeopleItemDto(id = 3, name = "Spike", profilePath = "path3"),
            createPeopleItemDto(id = 4, name = "Tyke", profilePath = "path4")
        )
    )
    private val secondPageForRandomizedTestWithSinglePerson = createMockPeopleResponse(
        totalPages = 2, results = listOf(
            createPeopleItemDto(id = 3, name = "Spike", profilePath = "path3")
        )
    )
    private val responseWithDuplicatesAndIncompleteData = createMockPeopleResponse(
        totalPages = 1, results = peopleWithDuplicatesAndIncompleteData
    )
    private val responseWithExtraData = createMockPeopleResponse(
        totalPages = 1, results = peopleWithExtraData
    )
    private val firstPageWithDuplicateId = createMockPeopleResponse(
        totalPages = 2,
        results = listOf(createPeopleItemDto(id = 1, name = "Tom", profilePath = "path1"))
    )
    private val secondPageWithDuplicateId = createMockPeopleResponse(
        totalPages = 2, results = peopleWithDuplicateId
    )
    private val responseWithIncompleteData = createMockPeopleResponse(
        totalPages = 1, results = peopleWithIncompleteData
    )
}