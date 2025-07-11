package com.smarthire.resume.infra;

import com.smarthire.resume.exception.*;
import com.smarthirepro.core.infra.FrameworkExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends FrameworkExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<RestErrorMessage> itemNotFoundHandler(ItemNotFoundException ex) {
        logger.warn("Item não pôde ser encontrado: {}", ex.getMessage());
        RestErrorMessage response = new RestErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidScoreWeightsException.class)
    public ResponseEntity<RestErrorMessage> invalidScoreWeightHandler(InvalidScoreWeightsException ex) {
        logger.warn("O peso definido para a vaga é inválido: {}", ex.getMessage());
        RestErrorMessage response = new RestErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}