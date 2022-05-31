package com.cpsai.appinessinteractive.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cpsai.appinessinteractive.model.PostsResponse
import com.cpsai.appinessinteractive.repository.ApiRepository
import kotlinx.coroutines.*
import retrofit2.Response

class FeedViewModel(private val apiRepository: ApiRepository) : ViewModel() {
    private var job: Job? = null
    private var loading = MutableLiveData<Boolean>()
    private val _postResponse = MutableLiveData<FilterEvent>()
    val postResponse : LiveData<FilterEvent> = _postResponse


    fun fetchPost(){
        loading.value = true
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = apiRepository.getPosts()
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    loading.value = false
                    Log.e("Response",""+response)
                    _postResponse.value = FilterEvent.FilterSuccessEvent(response.body())
                }else{
                    loading.value = false
                    _postResponse.value = FilterEvent.FilterFailureEvent(response)
                }
            }
        }
    }
}
sealed class FilterEvent{
    data class FilterSuccessEvent(val response: PostsResponse?): FilterEvent()
    data class FilterFailureEvent(val response: Response<PostsResponse>): FilterEvent()
}