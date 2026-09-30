package com.quotehunters.quotecollection.domain.quote.exception;

public class DuplicateQuoteException extends RuntimeException {
    public DuplicateQuoteException(String message) {
        super(message);
    }

    public DuplicateQuoteException() {
        super("이미 등록된 명언입니다.");
    }
}
