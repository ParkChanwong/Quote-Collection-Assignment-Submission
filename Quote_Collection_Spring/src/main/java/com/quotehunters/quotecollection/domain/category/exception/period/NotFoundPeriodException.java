package com.quotehunters.quotecollection.domain.category.exception.period;

public class NotFoundPeriodException extends RuntimeException {
    public NotFoundPeriodException(String message) {
        super(message);
    }

    public NotFoundPeriodException() {
        super("존재하지 않은 시대입니다.");
    }
}
