package com.smarthire.resume.exception;

public class InvalidPathException extends RuntimeException {
    public InvalidPathException() {
        super("O caminho passado é inválido");
    }
    public InvalidPathException(String message) {
        super(message);
    }
}
