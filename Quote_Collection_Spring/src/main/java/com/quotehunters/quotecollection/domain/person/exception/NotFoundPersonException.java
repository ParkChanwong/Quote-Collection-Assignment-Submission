package com.quotehunters.quotecollection.domain.person.exception;

public class NotFoundPersonException extends RuntimeException {
    public NotFoundPersonException(String message) {
        super(message);
    }

    public NotFoundPersonException() {
        super("존재하지 않은 인물입니다.");
    }
}
