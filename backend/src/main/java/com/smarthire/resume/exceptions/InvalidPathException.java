package com.smarthire.resume.exceptions;

public class InvalidPathException extends RuntimeException {
    public InvalidPathException() {
        super("Invalid path");
    }
    public InvalidPathException(String message) {
        super(message);
    }
}
