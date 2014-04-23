/*
 * Copyright (C) 2012 CyanogenMod
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

import android.content.ContentResolver;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.util.mahdi.DeviceUtils;

public class PowerMenu extends SettingsPreferenceFragment {
    private static final String TAG = "PowerMenu";

    private static final String KEY_ONTHEGO = "power_menu_onthego_enabled";

    private CheckBoxPreference mOnthegoPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.power_menu_settings);

        final ContentResolver resolver = getContentResolver();

        // Only enable on the go item if device has camera
        findPreference(Settings.System.POWER_MENU_ONTHEGO_ENABLED).setEnabled(
                DeviceUtils.hasCamera(getActivity()));

        mOnthegoPref = (CheckBoxPreference) findPreference(KEY_ONTHEGO);
        mOnthegoPref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_ONTHEGO_ENABLED, 0) == 1));

        // Only enable expanded desktop item if expanded desktop support is also enabled
        findPreference(Settings.System.POWER_MENU_GLOBAL_IMMERSIVE_MODE_ENABLED).setEnabled(
                Settings.System.getInt(resolver, Settings.System.GLOBAL_IMMERSIVE_MODE_STYLE, 0) != 0);

        // Only enable profiles item if System Profiles are also enabled
        findPreference(Settings.System.POWER_MENU_PROFILES_ENABLED).setEnabled(
                Settings.System.getInt(resolver, Settings.System.SYSTEM_PROFILES_ENABLED, 1) != 0);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        if (preference == mOnthegoPref) {
            value = mOnthegoPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_ONTHEGO_ENABLED,
                    value ? 1 : 0);
        } else {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        return true;
    }
}
