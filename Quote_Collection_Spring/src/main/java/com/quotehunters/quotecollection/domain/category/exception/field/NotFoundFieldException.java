package com.quotehunters.quotecollection.domain.category.exception.field;

public class NotFoundFieldException extends RuntimeException {
    public NotFoundFieldException(String message) {
        super(message);
    }

    public NotFoundFieldException() {
        super("존재하지 않는 분야입니다.");
    }
}
