package com.caitlynwiley.pettracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    CheckBoxPreference mDarkThemePref;
    Preference mSignOut;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        mDarkThemePref = findPreference("dark_theme_enabled");
        mSignOut = findPreference("sign_out");

        mDarkThemePref.setOnPreferenceChangeListener(this);
        mSignOut.setOnPreferenceClickListener(preference -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return true;
        });
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, container, false);
        ViewGroup innerContainer = v.findViewById(R.id.settings_frame);
        View innerView = super.onCreateView(inflater, innerContainer, savedInstanceState);
        if (innerView != null) {
            innerContainer.addView(innerView);
        }
        return v;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals("dark_theme_enabled")) {
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                    .putBoolean("dark_theme_enabled", (boolean) newValue).apply();
            getActivity().setTheme((boolean) newValue ? R.style.DarkTheme : R.style.LightTheme);
            getActivity().recreate();
            return true;
        }
        return false;
    }
}
