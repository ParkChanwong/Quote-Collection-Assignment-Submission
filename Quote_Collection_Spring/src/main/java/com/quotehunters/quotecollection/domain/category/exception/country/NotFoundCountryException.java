package com.quotehunters.quotecollection.domain.category.exception.country;

public class NotFoundCountryException extends RuntimeException {
    public NotFoundCountryException(String message) {
        super(message);
    }

    public NotFoundCountryException() {
        super("존재하지 않은 국가입니다.");
    }
}
