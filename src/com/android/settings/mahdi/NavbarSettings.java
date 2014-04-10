/*
 * Copyright (C) 2012 Slimroms
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.mahdi;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.internal.util.mahdi.DeviceUtils;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;
import com.android.settings.Utils;

public class NavbarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String TAG = "NavBar";

    private static final String CATEGORY_ADVANCED = "advanced_cat";
    private static final String PREF_MENU_LOCATION = "pref_navbar_menu_location";
    private static final String PREF_NAVBAR_MENU_DISPLAY = "pref_navbar_menu_display";
    private static final String PREF_NAVIGATION_BAR_CAN_MOVE = "navbar_can_move";
    private static final String EMULATE_MENU_KEY = "emulate_menu_key";
    private static final String KEY_NAVIGATION_BAR_LEFT = "navigation_bar_left";

    private int mNavBarMenuDisplayValue;

    ListPreference mMenuDisplayLocation;
    ListPreference mNavBarMenuDisplay;
    CheckBoxPreference mNavigationBarCanMove;
    CheckBoxPreference mEmulateMenuKey;
    CheckBoxPreference mNavigationBarLeftPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.navbar_settings);

        PreferenceScreen prefs = getPreferenceScreen();

        mMenuDisplayLocation = (ListPreference) findPreference(PREF_MENU_LOCATION);
        mMenuDisplayLocation.setValue(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.MENU_LOCATION,
                0) + "");
        mMenuDisplayLocation.setOnPreferenceChangeListener(this);

        mNavBarMenuDisplay = (ListPreference) findPreference(PREF_NAVBAR_MENU_DISPLAY);
        mNavBarMenuDisplayValue = Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.MENU_VISIBILITY,
                2);
        mNavBarMenuDisplay.setValue(mNavBarMenuDisplayValue + "");
        mNavBarMenuDisplay.setOnPreferenceChangeListener(this);

        mNavigationBarCanMove = (CheckBoxPreference) findPreference(PREF_NAVIGATION_BAR_CAN_MOVE);
        mNavigationBarCanMove.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.NAVIGATION_BAR_CAN_MOVE, 1) == 0);
        mNavigationBarCanMove.setOnPreferenceChangeListener(this);

        mEmulateMenuKey = (CheckBoxPreference) findPreference(EMULATE_MENU_KEY);
        mEmulateMenuKey.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.EMULATE_MENU_KEY, 0) == 1);
        mEmulateMenuKey.setOnPreferenceChangeListener(this);

        mNavigationBarLeftPref = (CheckBoxPreference) findPreference(KEY_NAVIGATION_BAR_LEFT);

        if (!Utils.isPhone(getActivity())) {
            PreferenceCategory advanced_cat =
                (PreferenceCategory) findPreference(CATEGORY_ADVANCED);
            advanced_cat.removePreference(mNavigationBarLeftPref);
            advanced_cat.removePreference(mNavigationBarCanMove);
        }
    }

    private void updateNavbarPreferences(boolean show) {
        mNavBarMenuDisplay.setEnabled(show);
        if (mNavigationBarCanMove != null) {
            mNavigationBarCanMove.setEnabled(show);
        }
        mMenuDisplayLocation.setEnabled(show
            && mNavBarMenuDisplayValue != 1);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mMenuDisplayLocation) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.MENU_LOCATION, Integer.parseInt((String) newValue));
            return true;
        } else if (preference == mNavBarMenuDisplay) {
            mNavBarMenuDisplayValue = Integer.parseInt((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.MENU_VISIBILITY, mNavBarMenuDisplayValue);
            mMenuDisplayLocation.setEnabled(mNavBarMenuDisplayValue != 1);
            return true;
        } else if (preference == mNavigationBarCanMove) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NAVIGATION_BAR_CAN_MOVE,
                    ((Boolean) newValue) ? 0 : 1);
            return true;
        } else if (preference == mEmulateMenuKey) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.EMULATE_MENU_KEY,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
