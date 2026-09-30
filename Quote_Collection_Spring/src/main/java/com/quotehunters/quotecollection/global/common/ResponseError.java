package com.quotehunters.quotecollection.global.common;

public class ResponseError {
    private int statusCode;
    private String message;

    public ResponseError() {}

    public ResponseError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseError{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}';
    }
}
