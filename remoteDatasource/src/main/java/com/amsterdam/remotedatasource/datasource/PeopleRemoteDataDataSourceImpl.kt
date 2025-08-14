package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.PeopleApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.PeopleRemoteDataSource
import com.amsterdam.repository.dto.remote.RemotePeopleItemDto
import com.amsterdam.repository.dto.remote.RemotePeopleResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class PeopleRemoteDataDataSourceImpl @Inject constructor(
    private val peopleApiService: PeopleApiService
) : PeopleRemoteDataSource {

    override suspend fun getTrendingPeople(page: Int): RemotePeopleResponse {
        return responseCall { peopleApiService.getTrendingPeople(page = page) }
    }

    override suspend fun getRandomizedTrendingPeople(requiredNumber: Int): List<RemotePeopleItemDto> {

        val totalPages = getTrendingPeople(FIRST_PAGE).totalPages
        val usedPages = mutableSetOf<Int>()
        val collectedPeople = mutableListOf<RemotePeopleItemDto>()


        while (collectedPeople.size < requiredNumber && usedPages.size < totalPages) {
            val peoples = (FIRST_PAGE..totalPages)
                .random()
                .also { usedPages.add(it) }
                .let { page -> getTrendingPeople(page).results }
                .filter(::onFilterHighQualityPeopleData)
                .shuffled()
                .distinctBy(RemotePeopleItemDto::id)


            for (people in peoples) {
                if (!collectedPeople.contains(people)) {
                    collectedPeople.add(people)
                    if (collectedPeople.size == requiredNumber) break
                }
            }
        }

        return collectedPeople
    }

    private fun onFilterHighQualityPeopleData(people: RemotePeopleItemDto): Boolean {
        return people.name.isNotBlank() && !people.fullPosterUrl.isNullOrBlank()
    }

    private companion object {
        const val FIRST_PAGE = 1
    }
}