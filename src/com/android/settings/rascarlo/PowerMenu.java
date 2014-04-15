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

package com.android.settings.rascarlo;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class PowerMenu extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {
    private static final String TAG = "PowerMenu";


    private static final String KEY_REBOOT = "power_menu_reboot";
    private static final String KEY_SCREENSHOT = "power_menu_screenshot";
    private static final String KEY_IMMERSIVE_MODE = "power_menu_immersive_mode";
    private static final String KEY_AIRPLANE = "power_menu_airplane";
    private static final String KEY_SILENT = "power_menu_silent";
    private static final String KEY_SCREENRECORD = "power_menu_screenrecord";
    private static final String POWER_MENU_ONTHEGO_ENABLED = "power_menu_onthego_enabled";
    private static final String KEY_ENABLE_POWER_MENU = "lockscreen_enable_power_menu";
    private static final String KEY_SEE_THROUGH = "see_through";

    private CheckBoxPreference mRebootPref;
    private CheckBoxPreference mScreenshotPref;
    ListPreference mImmersiveModePref;
    private CheckBoxPreference mAirplanePref;
    private CheckBoxPreference mSilentPref;
    private CheckBoxPreference mScreenrecordPref;
    private CheckBoxPreference mOnTheGoPowerMenu;
    private CheckBoxPreference mEnablePowerMenu;
    private CheckBoxPreference mSeeThrough;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.power_menu_settings);

        PreferenceScreen prefSet = getPreferenceScreen();
        mRebootPref = (CheckBoxPreference) findPreference(KEY_REBOOT);
        mRebootPref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_REBOOT_ENABLED, 1) == 1));

        mScreenshotPref = (CheckBoxPreference) findPreference(KEY_SCREENSHOT);
        mScreenshotPref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_SCREENSHOT_ENABLED, 0) == 1));

        mAirplanePref = (CheckBoxPreference) findPreference(KEY_AIRPLANE);
        mAirplanePref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_AIRPLANE_ENABLED, 1) == 1));

        mSilentPref = (CheckBoxPreference) findPreference(KEY_SILENT);
        mSilentPref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_SILENT_ENABLED, 1) == 1));

	mScreenrecordPref = (CheckBoxPreference) findPreference(KEY_SCREENRECORD);
        mScreenrecordPref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_SCREENRECORD_ENABLED, 0) == 1));

	// On the go
        mOnTheGoPowerMenu = (CheckBoxPreference) prefSet.findPreference(POWER_MENU_ONTHEGO_ENABLED);
        mOnTheGoPowerMenu.setChecked((Settings.System.getInt(getContentResolver(),
        Settings.System.POWER_MENU_ONTHEGO_ENABLED, 0) == 1));
        mOnTheGoPowerMenu.setOnPreferenceChangeListener(this);
	
	// lockscreen see through
        mSeeThrough = (CheckBoxPreference) prefSet.findPreference(KEY_SEE_THROUGH);
        if (mSeeThrough != null) {
            mSeeThrough.setChecked(Settings.System.getInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_SEE_THROUGH, 0) == 1);
        }

	mEnablePowerMenu = (CheckBoxPreference) findPreference(KEY_ENABLE_POWER_MENU);
	if (mEnablePowerMenu != null) {
	mEnablePowerMenu.setChecked(Settings.System.getInt(getContentResolver(),
        Settings.System.LOCKSCREEN_ENABLE_POWER_MENU, 1) == 1);
	mEnablePowerMenu.setOnPreferenceChangeListener(this);
	}

        mImmersiveModePref = (ListPreference) prefSet.findPreference(KEY_IMMERSIVE_MODE);
        mImmersiveModePref.setOnPreferenceChangeListener(this);
        int expandedDesktopValue = Settings.System.getInt(getContentResolver(), Settings.System.GLOBAL_IMMERSIVE_MODE_STYLE, 0);
        mImmersiveModePref.setValue(String.valueOf(expandedDesktopValue));
        updateExpandedDesktopSummary(expandedDesktopValue);
    }

 public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mImmersiveModePref) {
            int expandedDesktopValue = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.GLOBAL_IMMERSIVE_MODE_STYLE, expandedDesktopValue);
            updateExpandedDesktopSummary(expandedDesktopValue);
            return true;
 	} else if (preference == mEnablePowerMenu) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_ENABLE_POWER_MENU, (Boolean) newValue ? 1 : 0);
	return true;
        } else if (preference == mOnTheGoPowerMenu) {
            Settings.System.putInt(getContentResolver(),
	    Settings.System.POWER_MENU_ONTHEGO_ENABLED, (Boolean) newValue ? 1 : 0);
	return true;
	}
        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        if (preference == mScreenshotPref) {
            value = mScreenshotPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_SCREENSHOT_ENABLED,
                    value ? 1 : 0);
        } else if (preference == mRebootPref) {
            value = mRebootPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_REBOOT_ENABLED,
                    value ? 1 : 0);
 	} else if (preference == mScreenrecordPref) {
            value = mScreenrecordPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_SCREENRECORD_ENABLED,
                    value ? 1 : 0);
       } else if (preference == mAirplanePref) {
            value = mAirplanePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_AIRPLANE_ENABLED,
                    value ? 1 : 0);
       } else if (preference == mSilentPref) {
            value = mSilentPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_SILENT_ENABLED,
                    value ? 1 : 0);
	} else if (preference == mSeeThrough) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCKSCREEN_SEE_THROUGH,
                    mSeeThrough.isChecked() ? 1 : 0);
        } else {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        return true;
    }

 	private void updateExpandedDesktopSummary(int value) {
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
