package com.nicecode.android.tender.dto;

import com.google.gson.annotations.SerializedName;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 18.03.16.
 */

public class Response {

    @SerializedName("status_code")
    private Integer statusCode;

    @SerializedName("details")
    private Object details;

    @SerializedName("detail")
    private Object detail;

    @SerializedName("error")
    private String errorMessage;

    public Response(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }

    public boolean validate() {
        if (!this.statusCode.equals(0)) {
            this.errorMessage = "Error";
            return false;
        }
        return true;
    }
}
