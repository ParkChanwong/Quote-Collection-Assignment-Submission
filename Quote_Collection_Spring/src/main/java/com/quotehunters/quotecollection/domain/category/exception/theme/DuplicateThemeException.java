package com.quotehunters.quotecollection.domain.category.exception.theme;

public class DuplicateThemeException extends RuntimeException {
    public DuplicateThemeException(String message) {
        super(message);
    }

    public DuplicateThemeException() {
        super("이미 등록된 주제입니다.");
    }
}
