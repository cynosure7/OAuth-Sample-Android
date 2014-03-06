package com.ecs.android.sample.oauth2.store;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CredentialSharedPreference {
    public static final String KEY_PREFS_SMS_BODY = "code";
    private static final String APP_SHARED_PREFS = CredentialSharedPreference.class.getSimpleName(); //  Name of the file -.xml
    private SharedPreferences _sharedPrefs;
    private Editor _prefsEditor;

    public CredentialSharedPreference(Context context) {
        this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }

    public String getSmsBody() {
        return _sharedPrefs.getString(KEY_PREFS_SMS_BODY, "");
    }

    public void saveSmsBody(String text) {
        _prefsEditor.putString(KEY_PREFS_SMS_BODY, text);
        _prefsEditor.commit();
    }
}
