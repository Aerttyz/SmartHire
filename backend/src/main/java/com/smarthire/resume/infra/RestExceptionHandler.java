package com.smarthire.resume.infra;

import com.smarthire.resume.exceptions.EmptyPathException;
import com.smarthire.resume.exceptions.FlaskConnectionException;
import com.smarthire.resume.exceptions.InvalidPathException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EmptyPathException.class)
    private ResponseEntity<RestErrorMessage> emptyPathHandler(EmptyPathException ex) {
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(treatedResponse);
    }
    @ExceptionHandler(InvalidPathException.class)
    private ResponseEntity<RestErrorMessage> invalidPathHandler(InvalidPathException ex) {
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(treatedResponse);
    }
    @ExceptionHandler(FlaskConnectionException.class)
    private ResponseEntity<RestErrorMessage> flaskConnectionHandler(FlaskConnectionException ex) {
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(treatedResponse);
    }

}
