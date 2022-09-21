package com.atom.android.datastorage.ui.prefe_manager



import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.atom.android.datastorage.R


class Settings : PreferenceFragmentCompat() {
    val KEY_RESTORE_SETTING = "restore_setting"
    val TAG = "SettingPreferencesFragment"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

   /* private fun restoreDefaultSettings() {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
        PreferenceManager.setDefaultValues(activity, R.xml.preferences, true)
        preferenceScreen.removeAll()
        onCreatePreferences(null, null) //or onCreate(null) in your code
    }*/

}