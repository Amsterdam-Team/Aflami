package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RemotePeopleItemDto
import com.amsterdam.repository.dto.remote.RemotePeopleResponse

interface PeopleRemoteSource {
    suspend fun getTrendingPeople(page: Int): RemotePeopleResponse
    suspend fun getRandomizedTrendingPeople(
        requiredNumber: Int,
    ): List<RemotePeopleItemDto>
}