package com.nicecode.android.tender.dto;

import com.google.gson.annotations.SerializedName;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 21.03.16.
 */

public class ResponseUserLogin extends Response {

    @SerializedName("data")
    private UserLogin userLogin;

    public ResponseUserLogin(int statusCode, String errorMessage) {
        super(statusCode, errorMessage);
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }
}
