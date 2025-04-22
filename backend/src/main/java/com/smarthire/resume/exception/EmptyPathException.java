package com.smarthire.resume.exception;

public class EmptyPathException extends RuntimeException {
    public EmptyPathException() {
        super("Path is required");
    }

    public EmptyPathException(String message) {
        super(message);
    }
}
