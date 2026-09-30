package com.quotehunters.quotecollection.domain.account.exception;

public class EmptyPasswordException extends RuntimeException {
    public EmptyPasswordException(String message) {
        super(message);
    }

    public EmptyPasswordException() {
        super("비밀번호는 비어있을 수 없습니다.");
    }
}
