package com.example.submission2.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.submission2.R
import com.example.submission2.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_setting,
                container,
                false
        )

        childFragmentManager.beginTransaction().add(R.id.setting_holder, SettingPreferencesFragment()).commit()

        binding.btnBack.setOnClickListener {
            findNavController()
                    .navigateUp()
        }
        return binding.root
    }

}