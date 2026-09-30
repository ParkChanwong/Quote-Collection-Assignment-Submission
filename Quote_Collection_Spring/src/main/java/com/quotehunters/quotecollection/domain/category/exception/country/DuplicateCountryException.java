package com.quotehunters.quotecollection.domain.category.exception.country;

public class DuplicateCountryException extends RuntimeException {
    public DuplicateCountryException(String message) {
        super(message);
    }

    public DuplicateCountryException() {
        super("이미 등록된 국가입니다.");
    }
}
