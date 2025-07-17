package com.smarthire.resume.infra;

import com.smarthire.resume.exception.*;
import com.smarthirepro.core.infra.BaseExceptionHandler;
import com.smarthirepro.core.infra.BaseErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends BaseExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<BaseErrorMessage> itemNotFoundHandler(ItemNotFoundException ex) {
        logger.warn("Item não pôde ser encontrado: {}", ex.getMessage());
        BaseErrorMessage response = new BaseErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidScoreWeightsException.class)
    public ResponseEntity<BaseErrorMessage> invalidScoreWeightHandler(InvalidScoreWeightsException ex) {
        logger.warn("O peso definido para a vaga é inválido: {}", ex.getMessage());
        BaseErrorMessage response = new BaseErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}