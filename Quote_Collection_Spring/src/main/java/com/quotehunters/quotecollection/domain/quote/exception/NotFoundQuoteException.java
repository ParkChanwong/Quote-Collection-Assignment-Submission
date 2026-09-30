package com.quotehunters.quotecollection.domain.quote.exception;

public class NotFoundQuoteException extends RuntimeException {
    public NotFoundQuoteException(String message) {
        super(message);
    }

    public NotFoundQuoteException() {
        super("존재하지 않은 명언입니다.");
    }
}
