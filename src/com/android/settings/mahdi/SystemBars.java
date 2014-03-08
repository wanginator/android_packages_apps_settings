/*
 * Copyright (C) 2013 - 2014 The Mahdi Rom project
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

import android.content.res.Resources;
import android.os.RemoteException;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManagerGlobal;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class SystemBars extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String TAG = "SystemBars";

    private static final String KEY_STATUS_BAR = "status_bar";
    private static final String KEY_NAVIGATION_BAR = "navigation_bar";
    private static final String KEY_IMMERSIVE_MODE = "immersive_mode";

    private ListPreference mImmersiveModePref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.system_bars);

        PreferenceScreen prefSet = getPreferenceScreen();

        mImmersiveModePref = (ListPreference) prefSet.findPreference(KEY_IMMERSIVE_MODE);
        mImmersiveModePref.setOnPreferenceChangeListener(this);
        int immersiveModeValue = Settings.System.getInt(getContentResolver(), Settings.System.GLOBAL_IMMERSIVE_MODE_STYLE, 0);
        mImmersiveModePref.setValue(String.valueOf(immersiveModeValue));
        updateImmersiveModeSummary(immersiveModeValue);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mImmersiveModePref) {
            int immersiveModeValue = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.GLOBAL_IMMERSIVE_MODE_STYLE, immersiveModeValue);
            updateImmersiveModeSummary(immersiveModeValue);
            return true;
        }
        return false;
    }        

    private void updateImmersiveModeSummary(int value) {
        Resources res = getResources();

        if (value == 0) {
            /* expanded desktop deactivated */
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_GLOBAL_IMMERSIVE_MODE_ENABLED, 0);
            mImmersiveModePref.setSummary(res.getString(R.string.immersive_mode_disabled));
        } else if (value == 1) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_GLOBAL_IMMERSIVE_MODE_ENABLED, 1);
            String statusBarPresent = res.getString(R.string.immersive_mode_summary_status_bar);
            mImmersiveModePref.setSummary(res.getString(R.string.summary_immersive_mode, statusBarPresent));
        } else if (value == 2) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_GLOBAL_IMMERSIVE_MODE_ENABLED, 1);
            String statusBarPresent = res.getString(R.string.immersive_mode_summary_no_status_bar);
            mImmersiveModePref.setSummary(res.getString(R.string.summary_immersive_mode, statusBarPresent));
        }
    }
}
