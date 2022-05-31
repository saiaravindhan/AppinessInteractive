package com.cpsai.zohotask.network

import com.cpsai.appinessinteractive.model.PostsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("myHierarchy")
    suspend fun getPosts(): Response<PostsResponse>
}