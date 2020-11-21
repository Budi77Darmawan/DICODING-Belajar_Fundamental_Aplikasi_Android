package com.example.submission2.util.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {

    @GET("search/users")
    suspend fun getUserRequest(
        @Query("q") username: String?
    ): SearchResponse

    @GET("users/{username}")
    suspend fun detailUserRequest(
            @Path("username") username: String?
    ): DetailResponse

    @GET("users/{username}/followers")
    suspend fun getFollowersRequest(
        @Path("username") username: String?
    ): List<FollowResponse>

    @GET("users/{username}/following")
    suspend fun getFollowingRequest(
        @Path("username") username: String?
    ): List<FollowResponse>
}