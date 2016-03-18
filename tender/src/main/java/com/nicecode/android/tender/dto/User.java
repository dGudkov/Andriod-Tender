package com.nicecode.android.tender.dto;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 18.03.16.
 */

public class User {

    @SerializedName("id")
    private Integer userId;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String eMail;

    public User(LinkedTreeMap<String, Object> userMap) {
        this.userId = ((Double) userMap.get("id")).intValue();
        this.firstName = (String) userMap.get("first_name");
        this.lastName = (String) userMap.get("last_name");
        this.phone = (String) userMap.get("phone");
        this.eMail = (String) userMap.get("phone");

    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
}
