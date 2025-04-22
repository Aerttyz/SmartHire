package com.smarthire.resume.exception;

public class FlaskConnectionException extends RuntimeException {
    public FlaskConnectionException() {
        super("Connection to Flask service failed");
    }
    public FlaskConnectionException(String message) {
        super(message);
    }
}
