package com.quotehunters.quotecollection.global.common;

import java.util.List;

public class ResponseList<T> {
    private int statusCode;
    private List<T> result;

    public ResponseList() {}

    public ResponseList(int statusCode, List<T> result) {
        this.statusCode = statusCode;
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "statusCode=" + statusCode +
                ", result=" + result +
                '}';
    }
}
