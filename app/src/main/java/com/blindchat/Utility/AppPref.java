package com.blindchat.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.blindchat.BlindChatApplications;

import static android.content.Context.MODE_PRIVATE;

public class AppPref {
    private static AppPref instance;
    private final String NAME = "name";
    private final String ID="id";
    private final String EMAIL="email";
    private final String MOBILE="mobile";
    private final String UNIQID="uniqid";
    private final String PHOTO="photo";
    private final String POINTS="point";
    private final String TOKEN="token";

    private final String PASSCODE="passcode";


    private SharedPreferences sPreferences;
    private SharedPreferences.Editor sEditor;
    private String SG_SHARED_PREFERENCES = "shared_preferences";


    private AppPref(Context context) {

        sPreferences = context.getSharedPreferences(SG_SHARED_PREFERENCES,
                MODE_PRIVATE);
        sEditor = sPreferences.edit();
    }


    public static AppPref getInstance() {
        if (instance == null) {
            synchronized (AppPref.class) {
                if (instance == null) {
                    instance = new AppPref(BlindChatApplications.getInstance().getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void registerPre(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sPreferences.registerOnSharedPreferenceChangeListener(listener);
    }


    public void unRegister(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }


    public String getUSERID() {
        return sPreferences.getString(ID, "");
    }

    public void setUSERID(String id) {
        sEditor.putString(ID, id);
        sEditor.commit();
    }

    public String getUNIQID() {
        return sPreferences.getString(UNIQID, "");
    }

    public void setTOKEN(String token) {
        sEditor.putString(TOKEN, token);
        sEditor.commit();
    }

    public String getTOKEN() {
        return sPreferences.getString(TOKEN, "");
    }

    public void setUNIQID(String id) {
        sEditor.putString(UNIQID, id);
        sEditor.commit();
    }

    public String getPHOTO() {
        return sPreferences.getString(PHOTO, "");
    }

    public void setPHOTO(String photo) {
        sEditor.putString(PHOTO, photo);
        sEditor.commit();
    }


    public String getNAME() {
        return sPreferences.getString(NAME, "");
    }

    public void setNAME(String name) {
        sEditor.putString(NAME, name);
        sEditor.commit();
    }

    public String getEMAIL() {
        return sPreferences.getString(EMAIL, "");
    }

    public void setEMAIL(String email) {
        sEditor.putString(EMAIL, email);
        sEditor.commit();
    }


    public String getMOBILE() {
        return sPreferences.getString(MOBILE, "");
    }

    public void setMOBILE(String mobile) {
        sEditor.putString(MOBILE, mobile);
        sEditor.commit();
    }

    public String getPASSCODE() {
        return sPreferences.getString(PASSCODE, "");
    }

    public void setPASSCODE(String passcode) {
        sEditor.putString(PASSCODE, passcode);
        sEditor.commit();
    }

    public String getPOINTS() {
        return sPreferences.getString(POINTS, "");
    }

    public void setPOINTS(String points) {
        sEditor.putString(POINTS, points);
        sEditor.commit();
    }
}
