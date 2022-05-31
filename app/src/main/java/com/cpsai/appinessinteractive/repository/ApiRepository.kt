package com.cpsai.appinessinteractive.repository

import com.cpsai.zohotask.network.ApiHelper

class ApiRepository(private val apiHelper: ApiHelper) {
    suspend fun getPosts() = apiHelper.getPosts()
}