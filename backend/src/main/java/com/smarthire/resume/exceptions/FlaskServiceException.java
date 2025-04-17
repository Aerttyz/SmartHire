package com.smarthire.resume.exceptions;

public class FlaskServiceException extends RuntimeException {
    public FlaskServiceException() {
        super("Error connecting to Flask service");
    }
    public FlaskServiceException(String message) {
        super(message);
    }
}
