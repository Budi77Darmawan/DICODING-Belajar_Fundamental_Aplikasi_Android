package com.example.submission2.detail.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2.R
import com.example.submission2.databinding.FragmentFollowingBinding
import com.example.submission2.util.api.ApiClient
import com.example.submission2.util.api.FollowResponse
import com.example.submission2.util.api.GithubApiService

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var viewModel: FollowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_following, container, false)
        val username = arguments?.getString("Username")

        viewModel = ViewModelProvider(this).get(FollowViewModel::class.java)
        val service =
                ApiClient.getApiClient()?.create(GithubApiService::class.java)
        if (service != null) {
            viewModel.setGithubService(service)
        }
        viewModel.getFollowingsApi(username)
        subscribeLiveData()

        return binding.root
    }

    private fun showRecyclerList(list: List<FollowResponse>) {
        binding.rvFollowing.setHasFixedSize(true)
        val listFollowAdapter = ListFollowAdapter(list)
        binding.rvFollowing.adapter = listFollowAdapter
        binding.rvFollowing.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollowing.adapter?.notifyDataSetChanged()
    }

    private fun subscribeLiveData() {
        viewModel.followingLiveData.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                binding.rvFollowing.visibility = View.VISIBLE
                binding.descFollowing.visibility = View.GONE
                showRecyclerList(it)
            } else {
                binding.rvFollowing.visibility = View.GONE
                binding.descFollowing.visibility = View.VISIBLE
            }
        })

        viewModel.loadingLiveData.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

}