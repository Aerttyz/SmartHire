package com.smarthire.resume.exceptions;

public class FlaskConnectionException extends RuntimeException {
    public FlaskConnectionException() {
        super("Connection to Flask service failed");
    }
    public FlaskConnectionException(String message) {
        super(message);
    }
}
