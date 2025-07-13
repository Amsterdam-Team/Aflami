package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RemoteTvShowResponse

interface RemoteTvShowsDatasource {

    suspend fun getTvShowsByKeyword(
        keyword: String,
        rating: Int,
        categoryId: Long?
    ): RemoteTvShowResponse

}