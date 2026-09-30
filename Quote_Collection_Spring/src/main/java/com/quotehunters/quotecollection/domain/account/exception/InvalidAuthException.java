package com.quotehunters.quotecollection.domain.account.exception;

public class InvalidAuthException extends RuntimeException {
    public InvalidAuthException() {
        super("권한은 0(관리자) 또는 1(사용자)이어야 합니다.");
    }
}
