package com.nicecode.android.tender.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

public class Preferences {

    public static final String PREFERENCES = "pref";

    private SharedPreferences mPreferences;

    private final Gson mGson;

    public Preferences(Context context) {
        this.mGson = new Gson();
        this.mPreferences = context.getSharedPreferences(PREFERENCES, Activity.MODE_PRIVATE);
//        this.getWalkThroughtEnabledPreferences();
//        this.getUserPreferences();
    }

    @SuppressWarnings("unused")
    public void savePreferences() {
//        this.mPreferences.edit()
//                .putBoolean(PREFERENCES_WALK_THROUGHT_ENABLES, this.mWalkThroughtEnabled)
//                .commit();
    }

//    public Preferences getWalkThroughtEnabledPreferences() {
//        this.mWalkThroughtEnabled = this.mPreferences.getBoolean(PREFERENCES_WALK_THROUGHT_ENABLES, true);
//        return this;
//    }
//
//    public Preferences getUserPreferences() {
//        String userJson = this.mPreferences.getString(PREFERENCES_USER, null);
//        if (userJson != null) {
//            synchronized (mGson) {
//                this.mUser = mGson.fromJson(userJson, User.class);
//            }
//        } else {
//            this.mUser = new User();
//        }
//        return this;
//    }
//
//    @SuppressWarnings("unused")
//    public void setWalkThroughtEnabled(boolean walkThroughtEnabled) {
//        this.mWalkThroughtEnabled = walkThroughtEnabled;
//        this.mPreferences.edit()
//                .putBoolean(PREFERENCES_WALK_THROUGHT_ENABLES, this.mWalkThroughtEnabled)
//                .commit();
//    }
//
//    @SuppressWarnings("unused")
//    public boolean getWalkThroughtEnabled() {
//        return this.mWalkThroughtEnabled;
//    }
//
//    @SuppressWarnings("unused")
//    public void setUser(User user) {
//        String userJson = null;
//        if (user != null) {
//            this.mUser = user;
//            synchronized (mGson) {
//                userJson = mGson.toJson(user);
//            }
//        } else {
//            this.mUser = new User();
//        }
//        this.mPreferences.edit()
//                .putString(PREFERENCES_USER, userJson)
//                .commit();
//    }
//
//    @SuppressWarnings("unused")
//    public User getUser() {
//        return this.mUser;
//    }
}
