package com.cpsai.appinessinteractive.model

data class PostsResponse(
    val dataObject: List<DataObject>,
    val status: Boolean,
    val statusCode: Int
)