package com.smarthire.resume.exception;

public class FlaskConnectionException extends RuntimeException {
    public FlaskConnectionException() {
        super("Conexão com o serviço Flask falhou");
    }
    public FlaskConnectionException(String message) {
        super(message);
    }
}
