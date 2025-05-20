package com.smarthire.resume.infra;

import com.smarthire.resume.exception.*;
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
    @ExceptionHandler(BusinessRuleException.class)
        private ResponseEntity<RestErrorMessage> businessRuleHandler(BusinessRuleException ex) {
            RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(treatedResponse);
    }
    @ExceptionHandler(PersistenceException.class) 
    private ResponseEntity<RestErrorMessage> persistenceHandler(PersistenceException ex) {
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(treatedResponse);
    }
    @ExceptionHandler(ItemNotFoundException.class)
    private ResponseEntity<RestErrorMessage> itemNotFoundHandler(ItemNotFoundException ex) {
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(treatedResponse);
    }
    @ExceptionHandler(AuthenticationException.class)
    private ResponseEntity<RestErrorMessage> authenticationHandler(AuthenticationException ex) {
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(treatedResponse);
    }
    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<RestErrorMessage> userNotFoundHandler(UserNotFoundException ex) {
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(treatedResponse);
    }

}
