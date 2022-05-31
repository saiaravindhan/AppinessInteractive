package com.cpsai.zohotask.network

class ApiHelper(private val apiService: ApiService) {
    suspend fun getPosts() = apiService.getPosts()
}