package com.example.submission2.detail

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.submission2.R
import com.example.submission2.databinding.FragmentDetailBinding
import com.example.submission2.detail.viewpager.FollowersFragment
import com.example.submission2.detail.viewpager.FollowingFragment
import com.example.submission2.detail.viewpager.ViewPagerAdapter
import com.example.submission2.util.api.ApiClient
import com.example.submission2.util.api.GithubApiService
import com.example.submission2.util.db.DatabaseContract
import com.example.submission2.util.db.DatabaseContract.UsersColumns.Companion.CONTENT_URI
import com.example.submission2.util.db.UserHelper
import com.example.submission2.widget.AppWidget
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var dataUser: DetailUserModel
    private lateinit var userHelper: UserHelper
    private var isFav = false
    private lateinit var uriWithId: Uri
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        username = DetailFragmentArgs.fromBundle(arguments as Bundle).username
        val idUser = DetailFragmentArgs.fromBundle(arguments as Bundle).id

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        val service =
            ApiClient.getApiClient()?.create(GithubApiService::class.java)
        if (service != null) {
            viewModel.setGithubService(service)
        }
        viewModel.getUsersApi(username)
        viewModel.checkUserFavorite(requireContext(), idUser)
        subscribeLiveData()

        binding.btnBack.setOnClickListener {
            findNavController()
                .navigateUp()
        }

        userHelper = UserHelper.getInstance(requireContext())
        userHelper.open()

        binding.btnFav.setOnClickListener {
            if (isFav) {
                isFav = false
                deleteUserFav()
            } else {
                isFav = true
                addUserFav()
            }
        }

        val bundle = Bundle()
        bundle.putString("Username", username)
        val followersFragment = FollowersFragment()
        followersFragment.arguments = bundle
        val followingFragment = FollowingFragment()
        followingFragment.arguments = bundle

        val fragmentList = arrayListOf(
            followersFragment,
            followingFragment
        )

        val adapter = ViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)
        binding.vpProfile.adapter = adapter

        TabLayoutMediator(binding.TabLayout, binding.vpProfile) { tab, position ->
            tab.text =
                if (position == 0) getText(R.string.followers) else getText(R.string.following)
        }.attach()

        return binding.root
    }

    private fun subscribeLiveData() {
        viewModel.detailLiveData.observe(viewLifecycleOwner, {
            dataUser = it
            binding.apply {
                tvName.text = if (!it.name.isNullOrEmpty()) it.name else getText(R.string.full_name)
                tvUsername.text =
                    if (!it.login.isNullOrEmpty()) "@${it.login}" else getText(R.string.username)
                tvCity.text =
                    if (!it.location.isNullOrEmpty()) it.location else getText(R.string.address)
                tvCompany.text =
                    if (!it.company.isNullOrEmpty()) it.company else getText(R.string.company_name)
                if (!it.avatar_url.isNullOrEmpty()) {
                    Glide.with(this@DetailFragment)
                        .load(it.avatar_url)
                        .into(imgProfile)
                }
            }
        })

        viewModel.isFavLiveData.observe(viewLifecycleOwner, {
            isFav = it
            if (isFav) {
                binding.btnFav.setImageResource(R.drawable.ic_favorite)
            } else {
                binding.btnFav.setImageResource(R.drawable.ic_unfavorite)
            }
        })

        viewModel.loadingLiveData.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun addUserFav() {
        val values = ContentValues()
        values.put(DatabaseContract.UsersColumns._ID, dataUser.id)
        values.put(DatabaseContract.UsersColumns.USERNAME, dataUser.login)
        values.put(DatabaseContract.UsersColumns.TYPE, dataUser.type)
        values.put(DatabaseContract.UsersColumns.IMAGE, dataUser.avatar_url)

        requireContext().contentResolver.insert(CONTENT_URI, values)
        binding.btnFav.setImageResource(R.drawable.ic_favorite)
        showSnackBarMessage(getString(R.string.add_user_favorite))
        sendUpdateFavoriteList(requireContext())
    }

    private fun deleteUserFav() {
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + dataUser.id)
        requireContext().contentResolver.delete(uriWithId, null, null)
        binding.btnFav.setImageResource(R.drawable.ic_unfavorite)
        showSnackBarMessage(getString(R.string.delete_user_favorite))
        sendUpdateFavoriteList(requireContext())
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.toolbar, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun sendUpdateFavoriteList(context: Context) {
        val i = Intent(context, AppWidget::class.java)
        i.action = AppWidget.UPDATE_ITEM
        context.sendBroadcast(i)
    }
}

