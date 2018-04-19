package com.ggslk.ggslk.fragment

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.ggslk.ggslk.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.settings)
    }

    companion object {

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}
