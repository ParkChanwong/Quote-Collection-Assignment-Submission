package com.quotehunters.quotecollection.domain.category.exception.theme;

public class NotFoundThemeException extends RuntimeException {
    public NotFoundThemeException(String message) {
        super(message);
    }

    public NotFoundThemeException() {
        super("존재하지 않은 주제입니다.");
    }
}
