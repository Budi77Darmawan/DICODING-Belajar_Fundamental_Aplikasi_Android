package com.example.consumerapp.util.db

import android.net.Uri
import android.provider.BaseColumns

internal class DatabaseContract {

    class UsersColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user_favorite"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val TYPE = "type"
            const val IMAGE = "image"

            const val AUTHORITY = "com.example.submission2.util.db"
            private const val SCHEME = "content"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}