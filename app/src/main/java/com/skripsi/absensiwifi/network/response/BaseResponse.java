package com.skripsi.absensiwifi.network.response;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private T data;

    public String status;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}