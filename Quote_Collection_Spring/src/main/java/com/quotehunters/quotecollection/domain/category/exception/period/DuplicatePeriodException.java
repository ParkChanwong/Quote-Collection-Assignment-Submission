package com.quotehunters.quotecollection.domain.category.exception.period;

public class DuplicatePeriodException extends RuntimeException {
    public DuplicatePeriodException(String message) {
        super(message);
    }

    public DuplicatePeriodException() {
        super("이미 등록된 시대입니다.");
    }
}
