package com.smarthire.resume.exception;

public class PersistenceException extends RuntimeException{
    public PersistenceException() {
        super("Erro de persistência de dados");
    }
    public PersistenceException(String message) {
        super(message);
    }
}
