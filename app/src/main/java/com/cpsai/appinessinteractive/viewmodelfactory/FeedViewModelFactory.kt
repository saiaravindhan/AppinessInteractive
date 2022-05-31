package com.cpsai.appinessinteractive.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cpsai.appinessinteractive.repository.ApiRepository
import com.cpsai.appinessinteractive.viewmodel.FeedViewModel
import com.cpsai.zohotask.network.ApiHelper

class FeedViewModelFactory constructor(private val apiHelper: ApiHelper): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FeedViewModel::class.java)){
            FeedViewModel(ApiRepository(apiHelper)) as T
        }else{
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}