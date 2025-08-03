package com.amsterdam.remotedatasource.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface UserListApiService {
    @FormUrlEncoded
    @POST(ADD_MOVIE_TO_LIST)
    suspend fun addMediaItemToList(
        @Path("listId") listId: Long,
        @Field("media_type") mediaType: String,
        @Field("media_id") movieId: Int,
    )

    companion object {
        const val ADD_MOVIE_TO_LIST = "list/{listId}/items"
    }
}
