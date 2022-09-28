package com.atom.android.datastorage.ui.prefe_manager



import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.atom.android.datastorage.R


class Settings : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    private fun restoreDefaultSettings() {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
        PreferenceManager.setDefaultValues(requireContext(), R.xml.settings, true)
        preferenceScreen.removeAll()
        onCreatePreferences(null, null) //or onCreate(null) in your code
    }

}