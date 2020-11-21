package com.example.submission2.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2.R
import com.example.submission2.databinding.FragmentFavoriteBinding
import com.example.submission2.home.ListUsersAdapter
import com.example.submission2.home.UserModel
import com.example.submission2.util.db.DatabaseContract.UsersColumns.Companion.CONTENT_URI
import com.example.submission2.util.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)

        loadUsersAsync()

        binding.btnBack.setOnClickListener {
            findNavController()
                    .navigateUp()
        }

        return binding.root
    }

    private fun showRecyclerList(list: List<UserModel>) {
        binding.rvUsers.setHasFixedSize(true)
        val listUsersAdapter = ListUsersAdapter(list)
        binding.rvUsers.adapter = listUsersAdapter
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter?.notifyDataSetChanged()

        listUsersAdapter.setOnItemClickCallback(object :
        ListUsersAdapter.OnItemClickCallback {
            override fun onItemClicked(position: Int) {
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment()
                action.username = list[position].name.toString()
                action.id = list[position].id.toString()
                findNavController().navigate(action)
            }
        })
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredUsers = async(Dispatchers.IO) {
                val cursor = requireContext().contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val users = deferredUsers.await()
            if (users.size > 0) {
                showRecyclerList(users)
                binding.imgFav.visibility = View.GONE
                binding.descFav.visibility = View.GONE
                binding.descFav2.visibility = View.GONE
            } else {
                binding.imgFav.visibility = View.VISIBLE
                binding.descFav.visibility = View.VISIBLE
                binding.descFav2.visibility = View.VISIBLE
            }
            binding.progressBar.visibility = View.GONE
        }
    }

}