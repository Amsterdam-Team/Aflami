package com.amsterdam.remotedatasource.api

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
    @POST(CREATE_LIST)
    suspend fun createNewList(
        @Query(SESSION_ID) sessionId: String,
        @Field(NAME) listName: String,
        @Field(DESCRIPTION) description: String,
        @Field(LANGUAGE) language: String,
    ): CreateUserListResponse
    @GET(USER_LISTS_ENDPOINT)
    suspend fun getUserLists(
        @Path("account_id") accountId: Int = 0,
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
    ): RemoteUserListResponse

    @GET(GET_USER_LIST_DETAILS)
    suspend fun getMoviesFromList(
        @Path(LIST_ID) listId: Long,
        @Query(PAGE) page: Int,
    ): UserListDetailsResponse

    @DELETE(DELETE_LIST)
    suspend fun deleteList(
        @Path(LIST_ID) listId: Long,
        @Query(SESSION_ID) sessionId: String,
    )

    @FormUrlEncoded
    @POST(DELETE_MOVIE_FROM_LIST)
    suspend fun removeMovieFromList(
        @Path(LIST_ID) listId: Long,
        @Query(SESSION_ID) sessionId: String,
        @Field(MEDIA_ID) movieId: Long,
    )

    companion object {
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private const val LANGUAGE = "language"
        private const val PAGE = "page"
        private const val LIST_ID = "list_id"
        private const val SESSION_ID = "session_id"
        private const val MEDIA_ID = "media_id"
        private const val CREATE_LIST = "list"

        const val USER_LISTS_ENDPOINT = "account/{account_id}/lists"

        private const val GET_USER_LIST_DETAILS = "list/{list_id}"
        private const val DELETE_LIST = "list/{list_id}"
        private const val DELETE_MOVIE_FROM_LIST = "list/{list_id}/remove_item"
    }
}
