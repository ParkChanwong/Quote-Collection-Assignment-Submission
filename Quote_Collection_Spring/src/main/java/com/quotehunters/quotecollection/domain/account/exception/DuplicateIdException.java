package com.quotehunters.quotecollection.domain.account.exception;

public class DuplicateIdException extends RuntimeException {
    public DuplicateIdException(String message) {
        super(message);
    }

    public DuplicateIdException() {
        super("중복되는 ID가 있습니다.");
    }
}
