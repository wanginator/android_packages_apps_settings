/*
 * Copyright (C) 2013 SlimRoms Project
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

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import com.android.settings.rascarlo.lsn.LockscreenNotificationsPreference;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class LockscreenSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {
    private static final String TAG = "LockscreenSettings";
    private static final String KEY_SEE_THROUGH = "see_through";
    private static final String KEY_PEEK = "notification_peek";
    private static final String KEY_PEEK_PICKUP_TIMEOUT = "peek_pickup_timeout";
    private static final String KEY_LOCKSCREEN_NOTIFICATONS = "lockscreen_notifications";

    private CheckBoxPreference mSeeThrough;
    private SystemSettingCheckBoxPreference mNotificationPeek;
    private ListPreference mPeekPickupTimeout;
    private LockscreenNotificationsPreference mLockscreenNotifications;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.lockscreen_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

        // lockscreen see through
        mSeeThrough = (CheckBoxPreference) prefSet.findPreference(KEY_SEE_THROUGH);
        if (mSeeThrough != null) {
            mSeeThrough.setChecked(Settings.System.getInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_SEE_THROUGH, 0) == 1);
        }
        mLockscreenNotifications = (LockscreenNotificationsPreference) findPreference(KEY_LOCKSCREEN_NOTIFICATONS);

	mNotificationPeek = (SystemSettingCheckBoxPreference) findPreference(KEY_PEEK);

        mPeekPickupTimeout = (ListPreference) prefSet.findPreference(KEY_PEEK_PICKUP_TIMEOUT);
        int peekTimeout = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.PEEK_PICKUP_TIMEOUT, 0, UserHandle.USER_CURRENT);
        mPeekPickupTimeout.setValue(String.valueOf(peekTimeout));
        mPeekPickupTimeout.setSummary(mPeekPickupTimeout.getEntry());
        mPeekPickupTimeout.setOnPreferenceChangeListener(this);
    }
    @Override
    public boolean onPreferenceChange(Preference pref, Object value) {
	if (pref == mPeekPickupTimeout) {
            int peekTimeout = Integer.valueOf((String) value);
            Settings.System.putIntForUser(getContentResolver(),
                Settings.System.PEEK_PICKUP_TIMEOUT,
                    peekTimeout, UserHandle.USER_CURRENT);
            updatePeekTimeoutOptions(value);
            return true;
	}
    	    return false;
}

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
	boolean value;
	if (preference == mSeeThrough) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCKSCREEN_SEE_THROUGH,
                    mSeeThrough.isChecked() ? 1 : 0);
	return true;
	} else if (preference == mNotificationPeek) {
            Settings.System.putInt(getContentResolver(), Settings.System.PEEK_STATE,
                    mNotificationPeek.isChecked() ? 1 : 0);
	return true; 
	}else if (preference == mLockscreenNotifications) {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }        
		return super.onPreferenceTreeClick(preferenceScreen, preference);

    }

	private void updatePeekTimeoutOptions(Object newValue) {
        int index = mPeekPickupTimeout.findIndexOfValue((String) newValue);
        int value = Integer.valueOf((String) newValue);
        Settings.Secure.putInt(getActivity().getContentResolver(),
                Settings.System.PEEK_PICKUP_TIMEOUT, value);
        mPeekPickupTimeout.setSummary(mPeekPickupTimeout.getEntries()[index]);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
