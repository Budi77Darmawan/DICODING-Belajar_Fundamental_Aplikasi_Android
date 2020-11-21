package com.example.consumerapp.detail.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumerapp.R
import com.example.consumerapp.databinding.ItemListUsersBinding
import com.example.consumerapp.util.api.FollowResponse

class ListFollowAdapter(private val items: List<FollowResponse>) :
        RecyclerView.Adapter<ListFollowAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        return ListViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_list_users,
                        parent,
                        false
                )
        )

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = items[position]

        holder.binding.tvUsername.text = if (item.login.isNullOrEmpty()) "Full name" else item.login
        holder.binding.tvType.text = if (item.type.isNullOrEmpty()) "Job tittle" else item.type

        if (!item.avatar_url.isNullOrEmpty()) {
            Glide.with(holder.binding.root)
                    .load(item.avatar_url)
                    .into(holder.binding.imgProfile)
        }
    }

    inner class ListViewHolder(val binding: ItemListUsersBinding) :
            RecyclerView.ViewHolder(binding.root)
}