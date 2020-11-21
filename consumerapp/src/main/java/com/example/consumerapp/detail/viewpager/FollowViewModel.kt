package com.example.consumerapp.detail.viewpager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.consumerapp.util.api.FollowResponse
import com.example.consumerapp.util.api.GithubApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FollowViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: GithubApiService
    val followersLiveData = MutableLiveData(listOf<FollowResponse>())
    val followingLiveData = MutableLiveData(listOf<FollowResponse>())
    val loadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setGithubService(service: GithubApiService) {
        this.service = service
    }

    fun getFollowersApi(username: String?) {
        launch {
            loadingLiveData.value = true
            val response = service.getFollowersRequest(username)
            withContext(Dispatchers.Main) {
                try {
                    followersLiveData.value = response
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            loadingLiveData.value = false
        }
    }

    fun getFollowingsApi(username: String?) {
        launch {
            loadingLiveData.value = true
            val response = service.getFollowingRequest(username)
            withContext(Dispatchers.Main) {
                try {
                    followingLiveData.value = response
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            loadingLiveData.value = false
        }
    }
}
