package com.smarthire.resume.infra;

import com.smarthire.resume.exception.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorMessage> genericExceptionHandler(Exception ex) {
        RestErrorMessage treatedResponse = new RestErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro inesperado no servidor. Tente novamente mais tarde."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(treatedResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestErrorMessage> validationHandler(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação nos dados enviados.");

        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(treatedResponse);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<RestErrorMessage> dataAccessHandler(DataAccessException ex) {
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao acessar dados. Contate o suporte.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(treatedResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<RestErrorMessage> nullPointerHandler(NullPointerException ex) {
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado. Contate o suporte.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(treatedResponse);
    }

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
    @ExceptionHandler(InvalidScoreWeightsException.class)
    private ResponseEntity<RestErrorMessage> invalidScoreWeightHandler(InvalidScoreWeightsException ex) {
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(treatedResponse);
    }
}
