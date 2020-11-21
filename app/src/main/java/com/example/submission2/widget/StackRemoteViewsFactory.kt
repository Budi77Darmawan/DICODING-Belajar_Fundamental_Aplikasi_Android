package com.example.submission2.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.submission2.R
import com.example.submission2.home.UserModel
import com.example.submission2.util.db.DatabaseContract.UsersColumns.Companion.CONTENT_URI
import com.example.submission2.util.helper.MappingHelper

class StackRemoteViewsFactory(private val mContext: Context): RemoteViewsService.RemoteViewsFactory {

    private lateinit var mWidgetItems: List<UserModel>
    private var cursor: Cursor? = null

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        cursor?.close()

        val identityToken = Binder.clearCallingIdentity()

        val cursor = mContext.contentResolver?.query(CONTENT_URI, null, null, null, null)
        mWidgetItems = MappingHelper.mapCursorToArrayList(cursor)

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.item_widget)
        val bitmap = Glide.with(mContext)
            .asBitmap()
            .load(mWidgetItems[position].image)
            .submit(512, 512)
            .get()
        rv.setImageViewBitmap(R.id.imageView, bitmap)
        val extras = bundleOf(
            AppWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}