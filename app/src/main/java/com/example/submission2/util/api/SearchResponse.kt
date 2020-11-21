package com.example.submission2.util.api

import com.google.gson.annotations.SerializedName

class SearchResponse(val items: List<DataResult>?) {
    data class DataResult(
            @SerializedName("id") val id: String?,
            @SerializedName("login") val username: String?,
            @SerializedName("type") val type: String?,
            @SerializedName("avatar_url") val image: String?
    )
}