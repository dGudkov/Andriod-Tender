package com.nicecode.android.tender.utils;

import android.support.v4.util.Pair;

import com.nicecode.android.tender.dto.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

public class Utils {

    private static final String REQUEST_HOST = " http://kit.nicecode.biz";
    private static final String REQUEST_LOGIN_USER = "/api/v1/login/";
//    private static final String REQUEST_REGISTER_USER = "/create-user";
//    private static final String REQUEST_BIND_SOCIAL = "/set-socials";
//
    private static final String USER_NAME = "username";
    private static final String PASSWORD = "password";
//    private static final String FIRST_NAME = "name";
//    private static final String LAST_NAME = "surname";
//    private static final String EMAIL = "email";
//    private static final String GENDER = "gender";
//    private static final String SESSION_ID = "session_id";
//    private static final String SOCIAL_NAME = "social_name";
//    private static final String SOCIAL_ID = "social_id";

    public static final List<Pair<String, String>> requestJsonProperty = new ArrayList<>();
    public static final List<Pair<String, String>> requestImageProperty = new ArrayList<>();

    static {
        requestJsonProperty.add(new Pair<>("Accept", "application/JSON"));
    }

    public static Response LoginUser(String userName, String userPass) {
        List<Pair<String, String>> requestData = new ArrayList<>();
        requestData.add(new Pair<>(USER_NAME, userName));
        requestData.add(new Pair<>(PASSWORD, userPass));

        try {
            Response response = (Response) HTTPUtils.doPost(
                    REQUEST_HOST + REQUEST_LOGIN_USER,
                    requestJsonProperty, requestData);
            return response;
        } catch (Exception e) {
            return new Response(
                    1
            );
        }
    }
}
