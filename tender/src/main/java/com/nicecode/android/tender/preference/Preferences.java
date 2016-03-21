package com.nicecode.android.tender.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.nicecode.android.tender.dto.User;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

public class Preferences {

    public static final String PREFERENCES = "pref";

    private SharedPreferences mPreferences;

    private final Gson mGson;
    private User user;
    private String token;
    private boolean hasFilters = false;

    public Preferences(Context context) {
        this.mGson = new Gson();
        this.mPreferences = context.getSharedPreferences(PREFERENCES, Activity.MODE_PRIVATE);
    }

    @SuppressWarnings("unused")
    public void savePreferences() {
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setHasFilters(boolean hasFilters) {
        this.hasFilters = hasFilters;
    }

    public boolean isHasFilters() {
        return hasFilters;
    }
}
