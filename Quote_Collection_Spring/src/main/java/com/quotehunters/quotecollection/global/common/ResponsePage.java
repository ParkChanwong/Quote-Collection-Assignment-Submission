package com.quotehunters.quotecollection.global.common;

import java.util.List;

public class ResponsePage<T> {
    private int statusCode;
    private List<T> result;
    private long total;
    private int totalPage;

    public ResponsePage() {}

    public ResponsePage(int statusCode, List<T> result, long total, int totalPage) {
        this.statusCode = statusCode;
        this.result = result;
        this.total = total;
        this.totalPage = totalPage;
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

    public long getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "ResponsePage{" +
                "statusCode=" + statusCode +
                ", result=" + result +
                ", total=" + total +
                ", totalPage=" + totalPage +
                '}';
    }
}
