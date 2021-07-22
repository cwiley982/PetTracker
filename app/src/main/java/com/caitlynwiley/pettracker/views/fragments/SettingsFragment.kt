package com.caitlynwiley.pettracker.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    private var mDarkThemePref: SwitchPreference? = null
    private var mSignOut: Preference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDarkThemePref = findPreference("dark_theme_enabled")
        mSignOut = findPreference("sign_out")
        mDarkThemePref!!.onPreferenceChangeListener = this
        mSignOut!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("logged_in", false).apply()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.settings_fragment, container, false)
        val innerContainer = v.findViewById<ViewGroup>(R.id.settings_frame)
        val innerView = super.onCreateView(inflater, innerContainer, savedInstanceState)
        if (innerView != null) {
            innerContainer.addView(innerView)
        }
        return v
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        if (preference.key == "dark_theme_enabled") {
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putBoolean("dark_theme_enabled", newValue as Boolean).apply()
            requireActivity().setTheme(if (newValue) R.style.DarkTheme else R.style.LightTheme)
            requireActivity().recreate()
            return true
        }
        return false
    }
}