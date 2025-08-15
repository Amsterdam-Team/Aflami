package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RemotePeopleItemDto

interface PeopleRemoteDataSource {
    suspend fun getRandomizedTrendingPeople(
        requiredNumber: Int,
    ): List<RemotePeopleItemDto>
}