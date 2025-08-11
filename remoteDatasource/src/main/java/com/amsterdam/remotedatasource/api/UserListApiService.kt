package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.AddItemToListResponse
import com.amsterdam.repository.dto.remote.CreateUserListResponse
import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import com.amsterdam.repository.dto.remote.UserListDetailsResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserListApiService {
    @FormUrlEncoded
    @POST("list")
    suspend fun createNewList(
        @Query("session_id") sessionId: String,
        @Field("name") listName: String,
        @Field("description") description: String,
        @Field("language") language: String,
    ): CreateUserListResponse

    @GET("account/{account_id}/lists")
    suspend fun getUserLists(
        @Path("account_id") accountId: Int = 0,
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
    ): RemoteUserListResponse

    @GET("list/{list_id}")
    suspend fun getMoviesAndTvShowsFromList(
        @Path("list_id") listId: Long,
        @Query("page") page: Int,
    ): UserListDetailsResponse

    @DELETE("list/{list_id}")
    suspend fun deleteList(
        @Path("list_id") listId: Long,
        @Query("session_id") sessionId: String,
    )

    @FormUrlEncoded
    @POST("list/{list_id}/add_item")
    suspend fun addMediaItemToList(
        @Path("list_id") listId: Long,
        @Query("session_id") sessionId: String,
        @Field("media_id") movieId: Int,
    ): AddItemToListResponse

    @FormUrlEncoded
    @POST("list/{list_id}/remove_item")
    suspend fun removeMovieFromList(
        @Path("list_id") listId: Long,
        @Query("session_id") sessionId: String,
        @Field("media_id") movieId: Long,
    )

}
