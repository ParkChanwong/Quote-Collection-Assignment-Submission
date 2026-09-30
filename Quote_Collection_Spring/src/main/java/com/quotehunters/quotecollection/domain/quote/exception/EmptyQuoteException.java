package com.quotehunters.quotecollection.domain.quote.exception;

public class EmptyQuoteException extends RuntimeException {
    public EmptyQuoteException(String message) {
        super(message);
    }

    public EmptyQuoteException() {
        super("명언은 비어있을 수 없습니다.");
    }
}
