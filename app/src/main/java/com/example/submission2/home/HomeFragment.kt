package com.example.submission2.home

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2.MainActivity
import com.example.submission2.R
import com.example.submission2.databinding.FragmentHomeBinding
import com.example.submission2.util.api.ApiClient
import com.example.submission2.util.api.GithubApiService
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var search = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        (activity as MainActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val service =
            ApiClient.getApiClient()?.create(GithubApiService::class.java)
        if (service != null) {
            viewModel.setGithubService(service)
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    search = newText.toLowerCase(Locale.getDefault())
                    subscribeLiveData()
                    setView(1)
                } else {
                    search = ""
                    setView(0)
                    setView(3)
                }
                return true
            }
        })

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
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                action.username = list[position].name.toString()
                action.id = list[position].id.toString()
                findNavController().navigate(action)
            }
        })
    }

    private fun subscribeLiveData() {
        viewModel.getUsersApi(search)
        viewModel.usersLiveData.observe(this, {
            showRecyclerList(it)
            if (!it.isNullOrEmpty()) {
                setView(2)
            } else {
                setView(3)
            }
        })

        viewModel.loadingLiveData.observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun setView(v: Int) {
        when (v) {
            0 -> {
                binding.imgSearch.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.il_home_search
                    )
                )
                binding.descSearch.text = resources.getString(R.string.desc_search)
                binding.descSearch2.text = resources.getString(R.string.desc_search2)
            }
            1 -> {
                binding.imgSearch.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.il_home_search_not_found
                    )
                )
                binding.descSearch.text = resources.getString(R.string.search_not_found)
                binding.descSearch2.text = resources.getString(R.string.desc_not_found)
            }
            2 -> {
                binding.rvUsers.visibility = View.VISIBLE
                binding.imgSearch.visibility = View.GONE
                binding.descSearch.visibility = View.GONE
                binding.descSearch2.visibility = View.GONE
            }
            else -> {
                binding.rvUsers.visibility = View.GONE
                binding.imgSearch.visibility = View.VISIBLE
                binding.descSearch.visibility = View.VISIBLE
                binding.descSearch2.visibility = View.VISIBLE
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
        }
        if (item.itemId == R.id.action_favorite) {
            findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}