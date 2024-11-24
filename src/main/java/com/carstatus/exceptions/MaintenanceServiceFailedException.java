package com.carstatus.exceptions;

public class MaintenanceServiceFailedException extends RuntimeException{

    public MaintenanceServiceFailedException(String message) {
        super(message);
    }
}
