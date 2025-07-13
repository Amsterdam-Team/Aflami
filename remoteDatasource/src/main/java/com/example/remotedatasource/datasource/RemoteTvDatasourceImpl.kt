package com.example.remotedatasource.datasource

import android.util.Log
import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.client.safeCall
import com.example.remotedatasource.utils.Constant.BASE_URL
import com.example.repository.datasource.remote.RemoteTvShowsDatasource
import com.example.repository.dto.remote.RemoteTvShowResponse
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class RemoteTvDatasourceImpl(private val ktorClient: KtorClient) : RemoteTvShowsDatasource {

    override suspend fun getTvShowsByKeyword(
        keyword: String,
        rating: Int,
        categoryId: Long?
    ): RemoteTvShowResponse {
        return safeCall<RemoteTvShowResponse> {
            val selectedCategoryId: String = categoryId?.toString() ?: ""
            val response = ktorClient.get("$BASE_URL/search/tv?query=$keyword")
            Log.d("tv", response.bodyAsText())
            return Json.decodeFromString<RemoteTvShowResponse>(response.bodyAsText())
        }
    }
}