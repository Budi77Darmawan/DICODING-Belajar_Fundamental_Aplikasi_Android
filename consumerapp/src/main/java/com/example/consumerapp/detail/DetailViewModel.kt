package com.example.consumerapp.detail

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.consumerapp.util.api.DetailResponse
import com.example.consumerapp.util.api.GithubApiService
import com.example.consumerapp.util.db.DatabaseContract
import com.example.consumerapp.util.helper.MappingHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: GithubApiService
    val detailLiveData = MutableLiveData<DetailUserModel>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val isFavLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setGithubService(service: GithubApiService) {
        this.service = service
    }

    fun getUsersApi(username: String?) {
        launch {
            loadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.detailUserRequest(username)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is DetailResponse) {
                detailLiveData.value = DetailUserModel(
                        response.id,
                        response.login,
                        response.avatar_url,
                        response.name,
                        response.company,
                        response.location,
                        response.type
                )
            }
            loadingLiveData.value = false
        }
    }

    fun checkUserFavorite(mContext: Context, id: String?) {
        launch(Dispatchers.Main) {
            val deferredUsers = async(Dispatchers.IO) {
                val uriWithId = Uri.parse(DatabaseContract.UsersColumns.CONTENT_URI.toString() + "/" + id)
                val cursor = mContext.contentResolver?.query(uriWithId, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val users = deferredUsers.await()
            isFavLiveData.value = users.isNotEmpty()
        }
    }
}