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

    @SerializedName("data")
    private Object data;

    @SerializedName("details")
    private Object details;

    @SerializedName("error")
    private String errorMessage;

    public Response(int statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
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

    public boolean validate() {
        if (!this.statusCode.equals(0)) {
            this.errorMessage = "Error";
            return false;
        }
        return true;
    }
}
