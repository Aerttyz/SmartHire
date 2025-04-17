package com.smarthire.resume.exceptions;

public class EmptyPathException extends RuntimeException {
    public EmptyPathException() {
        super("Path is required");
    }

    public EmptyPathException(String message) {
        super(message);
    }
}
