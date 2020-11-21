package com.example.submission2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.example.submission2.databinding.FragmentSplashBinding
import java.util.*

class SplashFragment : PreferenceFragmentCompat() {
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)

        val topAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.top_anim)
        val bottomAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_anim)

        binding.apply {
            imgLogo.animation = topAnim
            tvSub.animation = bottomAnim
            tvDesc.animation = bottomAnim
        }

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }, 3000)

        return binding.root
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val mLanguage = resources.getString(R.string.key_language)
        val languageCode = preferenceManager.sharedPreferences.getString(mLanguage, "en")
        val dm= resources.displayMetrics
        val config = resources.configuration
        if (languageCode != null) {
            config.setLocale(Locale(languageCode.toLowerCase(Locale.ROOT)))
        }
        resources.updateConfiguration(config, dm)
    }
}