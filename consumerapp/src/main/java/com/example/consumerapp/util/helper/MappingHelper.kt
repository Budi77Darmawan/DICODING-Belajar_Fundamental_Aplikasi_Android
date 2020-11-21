package com.example.consumerapp.util.helper

import android.database.Cursor
import com.example.consumerapp.favorite.UserModel
import com.example.consumerapp.util.db.DatabaseContract

object MappingHelper {

    fun mapCursorToArrayList(usersCursor: Cursor?): ArrayList<UserModel> {
        val usersList = ArrayList<UserModel>()
        usersCursor?.apply {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(DatabaseContract.UsersColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UsersColumns.USERNAME))
                val type = getString(getColumnIndexOrThrow(DatabaseContract.UsersColumns.TYPE))
                val image = getString(getColumnIndexOrThrow(DatabaseContract.UsersColumns.IMAGE))
                usersList.add(UserModel(id, username, type, image))
            }
        }
        return usersList
    }
}