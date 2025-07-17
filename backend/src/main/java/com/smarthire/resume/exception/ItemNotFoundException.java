package com.smarthire.resume.exception;

import com.smarthirepro.core.exception.FrameworkBaseException;
import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends FrameworkBaseException {
    public ItemNotFoundException(String item, Object id) {
        super(item + " com identificador " + id + " não foi encontrado.", HttpStatus.NOT_FOUND);
    }
}
