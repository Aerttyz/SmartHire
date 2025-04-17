package com.smarthire.resume.infra;

import com.smarthire.resume.exceptions.EmptyPathException;
import com.smarthire.resume.exceptions.FlaskServiceException;
import com.smarthire.resume.exceptions.InvalidPathException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EmptyPathException.class)
    private ResponseEntity<String> emptyPathHandler(EmptyPathException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Path is required");
    }
    @ExceptionHandler(InvalidPathException.class)
    private ResponseEntity<String> invalidPathHandler(InvalidPathException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid path");
    }
    @ExceptionHandler(FlaskServiceException.class)
    private ResponseEntity<String> flaskConnectionHandler(FlaskServiceException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to connect to server");
    }

}
