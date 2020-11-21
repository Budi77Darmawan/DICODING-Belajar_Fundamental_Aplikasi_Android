package com.example.submission2.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission2.util.api.GithubApiService
import com.example.submission2.util.api.SearchResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HomeViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: GithubApiService
    val usersLiveData = MutableLiveData(listOf<UserModel>())
    val loadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setGithubService(service: GithubApiService) {
        this.service = service
    }

    fun getUsersApi(username: String) {
        launch {
            loadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getUserRequest(username)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is SearchResponse) {
                usersLiveData.value = response.items?.map {
                    UserModel(
                            it.id.orEmpty(),
                            it.username.orEmpty(),
                            it.type.orEmpty(),
                            it.image.orEmpty()
                    )
                }
            }
            loadingLiveData.value = false
        }
    }
}