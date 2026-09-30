package com.quotehunters.quotecollection.domain.category.exception.period;

public class EmptyPeriodException extends RuntimeException {
    public EmptyPeriodException(String message) {
        super(message);
    }

    public EmptyPeriodException() {
        super("시대명은 비어있을 수 없습니다.");
    }
}
