package com.example.consumerapp.detail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailUserModel(
        val id: String?,
        val login: String?,
        val avatar_url: String?,
        val name: String?,
        val company: String?,
        val location: String?,
        val type: String?
) : Parcelable