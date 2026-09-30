package com.quotehunters.quotecollection.global.common;

public class ResponseSingle<T> {
    private int statusCode;
    private T result;

    public ResponseSingle() {}

    public ResponseSingle(int statusCode, T result) {
        this.statusCode = statusCode;
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResponseSingle{" +
                "statusCode=" + statusCode +
                ", result=" + result +
                '}';
    }
}
