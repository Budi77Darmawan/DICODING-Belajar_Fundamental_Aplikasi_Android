package com.example.submission2.detail.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2.R
import com.example.submission2.databinding.FragmentFollowersBinding
import com.example.submission2.util.api.ApiClient
import com.example.submission2.util.api.FollowResponse
import com.example.submission2.util.api.GithubApiService

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding
    private lateinit var viewModel: FollowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_followers, container, false)
        val username = arguments?.getString("Username")

        viewModel = ViewModelProvider(this).get(FollowViewModel::class.java)
        val service =
            ApiClient.getApiClient()?.create(GithubApiService::class.java)
        if (service != null) {
            viewModel.setGithubService(service)
        }
        viewModel.getFollowersApi(username)
        subscribeLiveData()

        return binding.root
    }

    private fun showRecyclerList(list: List<FollowResponse>) {
        binding.rvFollowers.setHasFixedSize(true)
        val listFollowAdapter = ListFollowAdapter(list)
        binding.rvFollowers.adapter = listFollowAdapter
        binding.rvFollowers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollowers.adapter?.notifyDataSetChanged()
    }

    private fun subscribeLiveData() {
        viewModel.followersLiveData.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                binding.rvFollowers.visibility = View.VISIBLE
                binding.descFollowers.visibility = View.GONE
                showRecyclerList(it)
            } else {
                binding.rvFollowers.visibility = View.GONE
                binding.descFollowers.visibility = View.VISIBLE
            }
        })

        viewModel.loadingLiveData.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }
}