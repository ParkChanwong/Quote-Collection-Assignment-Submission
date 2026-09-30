package com.quotehunters.quotecollection.domain.account.exception;

public class SignInFailedException extends RuntimeException {
    public SignInFailedException(String message) {
        super(message);
    }
}
