package com.quotehunters.quotecollection.domain.category.exception.country;

public class EmptyCountryException extends RuntimeException {
    public EmptyCountryException(String message) {
        super(message);
    }

    public EmptyCountryException() {
        super("국가명은 비어있을 수 없습니다.");
    }
}
