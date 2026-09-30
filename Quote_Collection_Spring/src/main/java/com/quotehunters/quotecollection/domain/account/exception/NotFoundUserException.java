package com.quotehunters.quotecollection.domain.account.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(String message) {
        super(message);
    }

    public NotFoundUserException() {
        super("존재하지 않는 유저입니다.");
    }
}
