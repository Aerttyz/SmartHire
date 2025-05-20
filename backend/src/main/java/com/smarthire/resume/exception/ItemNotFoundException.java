package com.smarthire.resume.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String item, Object id) {
        super(item + " com identificador " + id + " não foi encontrado.");
    }
}
