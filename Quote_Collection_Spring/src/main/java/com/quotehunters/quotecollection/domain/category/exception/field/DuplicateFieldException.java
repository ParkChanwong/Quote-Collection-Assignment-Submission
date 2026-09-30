package com.quotehunters.quotecollection.domain.category.exception.field;

public class DuplicateFieldException extends RuntimeException {
    public DuplicateFieldException(String message) {
        super(message);
    }

    public DuplicateFieldException() {
        super("이미 등록된 분야입니다.");
    }
}
