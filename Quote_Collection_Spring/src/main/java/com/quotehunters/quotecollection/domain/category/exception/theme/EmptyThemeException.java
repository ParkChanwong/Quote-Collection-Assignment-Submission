package com.quotehunters.quotecollection.domain.category.exception.theme;

public class EmptyThemeException extends RuntimeException {
    public EmptyThemeException(String message) {
        super(message);
    }

    public EmptyThemeException() {
        super("주제명은 비어있을 수 없습니다.");
    }
}
