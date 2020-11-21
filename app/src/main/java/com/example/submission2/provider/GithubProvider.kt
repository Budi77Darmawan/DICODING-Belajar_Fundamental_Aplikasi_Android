package com.example.submission2.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.submission2.util.db.DatabaseContract.UsersColumns.Companion.AUTHORITY
import com.example.submission2.util.db.DatabaseContract.UsersColumns.Companion.CONTENT_URI
import com.example.submission2.util.db.DatabaseContract.UsersColumns.Companion.TABLE_NAME
import com.example.submission2.util.db.UserHelper

class GithubProvider : ContentProvider() {

    companion object {
        private const val USERS = 1
        private const val USERS_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userHelper: UserHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USERS)
            sUriMatcher.addURI(AUTHORITY,
                    "$TABLE_NAME/#",
                    USERS_ID)
        }
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        return if (sUriMatcher.match(uri) == USERS) userHelper.queryAll() else userHelper.queryById(uri.lastPathSegment.toString())
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USERS) {
            sUriMatcher.match(uri) -> userHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val deleted: Int = when (USERS_ID) {
            sUriMatcher.match(uri) -> userHelper.deleteByID(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}