package com.example.consumerapp.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumerapp.R
import com.example.consumerapp.databinding.ItemListUsersBinding

class ListUsersAdapter(private val items: List<UserModel>) :
    RecyclerView.Adapter<ListUsersAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(position: Int)
    }

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

        holder.binding.tvUsername.text = if (item.name.isNullOrEmpty()) "Full name" else item.name
        holder.binding.tvType.text = if (item.type.isNullOrEmpty()) "Type User" else item.type

        if (!item.image.isNullOrEmpty()) {
            Glide.with(holder.binding.root)
                .load(item.image)
                .into(holder.binding.imgProfile)
        }

        holder.binding.layout.setOnClickListener{
            onItemClickCallback.onItemClicked(position)
        }
    }

    inner class ListViewHolder(val binding: ItemListUsersBinding) :
        RecyclerView.ViewHolder(binding.root)
}