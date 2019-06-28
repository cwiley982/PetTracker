package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class SettingsFragment extends Fragment {

    View mFragView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.settings_fragment, container, false);

        final CheckBox darkThemePref = mFragView.findViewById(R.id.dark_theme_setting);
        darkThemePref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox darkThemeSetting = (CheckBox) v;
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                        .putBoolean("dark_theme_enabled", darkThemeSetting.isChecked()).apply();
            }
        });

        return mFragView;
    }
}
