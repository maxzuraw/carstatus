package com.carstatus.exceptions;

public class InsuranceServiceFailedException extends RuntimeException {

    public InsuranceServiceFailedException(String message) {
        super(message);
    }
}
