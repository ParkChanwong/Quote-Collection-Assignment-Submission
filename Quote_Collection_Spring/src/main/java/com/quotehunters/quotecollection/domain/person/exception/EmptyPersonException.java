package com.quotehunters.quotecollection.domain.person.exception;

public class EmptyPersonException extends RuntimeException {
    public EmptyPersonException(String message) {
        super(message);
    }

    public EmptyPersonException() {
        super("인물명은 비어있을 수 없습니다.");
    }
}
