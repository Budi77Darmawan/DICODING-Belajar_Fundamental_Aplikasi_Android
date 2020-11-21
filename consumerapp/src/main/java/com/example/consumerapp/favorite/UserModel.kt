package com.example.consumerapp.favorite

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
        val id: String?,
        val name: String?,
        val type: String?,
        val image: String?
) : Parcelable