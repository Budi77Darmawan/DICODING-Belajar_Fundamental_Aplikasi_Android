package com.example.submission2.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.*
import com.example.submission2.setting.alarmmanager.AlarmReceiver
import com.example.submission2.R
import java.util.*

class SettingPreferencesFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var reminder: String
    private lateinit var mLanguage: String
    private lateinit var isReminder: SwitchPreference
    private lateinit var isLanguage: ListPreference

    private var alarmReceiver = AlarmReceiver()

    companion object {
        private const val DEFAULT_LANGUAGE = "en"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)
        init()
        setSummaries()
    }

    private fun init() {
        reminder = resources.getString(R.string.key_reminder)
        mLanguage = resources.getString(R.string.key_language)
        isLanguage = findPreference<ListPreference>(mLanguage) as ListPreference
        isReminder = findPreference<SwitchPreference>(reminder) as SwitchPreference
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        isReminder.isChecked = sh.getBoolean(reminder, false)
        val language = sh.getString(mLanguage, DEFAULT_LANGUAGE)
        isLanguage.summary = if (language == "en") getString(R.string.language_en) else getString(R.string.language_id)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == reminder) {
            if (sharedPreferences != null) {
                val reminderAlarm = sharedPreferences.getBoolean(reminder, false)
                isReminder.isChecked = reminderAlarm
                alarmReminder(reminderAlarm)
            }
        }
        if (key == mLanguage) {
            if (sharedPreferences != null) {
                val language = sharedPreferences.getString(mLanguage, DEFAULT_LANGUAGE)
                isLanguage.summary = if (language == "en") getString(R.string.language_en) else getString(R.string.language_id)
                setAppLocale(language.toString())
                findNavController().navigate(R.id.action_settingFragment_self2)
            }
        }
    }

    private fun setAppLocale(languageCode: String) {
        val dm= resources.displayMetrics
        val config = resources.configuration
        config.setLocale(Locale(languageCode.toLowerCase(Locale.ROOT)))
        resources.updateConfiguration(config, dm)
    }

    private fun alarmReminder(active: Boolean) {
        if (active) {
            alarmReceiver.setReminderAlarm(requireContext())
        } else {
            alarmReceiver.cancelAlarm(requireContext())
        }
    }
}