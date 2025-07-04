package com.smarthire.resume.infra;

import com.smarthire.resume.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorMessage> genericExceptionHandler(Exception ex) {
        RestErrorMessage treatedResponse = new RestErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro inesperado no servidor. Tente novamente mais tarde."
        );
        logger.error("Erro inesperado no servidor: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(treatedResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<RestErrorMessage> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = String.format("Parâmetro obrigatório ausente: %s", ex.getParameterName());
        logger.warn(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestErrorMessage(HttpStatus.BAD_REQUEST, message));
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<RestErrorMessage> methodNotSupportedHandler(HttpRequestMethodNotSupportedException ex) {
        logger.warn("Método HTTP não suportado: {}", ex.getMethod());
        RestErrorMessage response = new RestErrorMessage(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP não permitido.");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RestErrorMessage> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logger.warn("Corpo da requisição malformado ou inválido: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RestErrorMessage(HttpStatus.BAD_REQUEST, "Corpo da requisição malformado ou inválido."));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<RestErrorMessage> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        logger.warn("Recurso não encontrado (rota): {}", ex.getRequestURL());
        RestErrorMessage response = new RestErrorMessage(HttpStatus.NOT_FOUND, "Recurso não encontrado.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestErrorMessage> validationHandler(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação nos dados enviados.");

        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, message);
        logger.warn("Erro de validação: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(treatedResponse);
    }
    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<RestErrorMessage> handleFileProcessing(FileProcessingException ex) {
        logger.error("Erro ao processar arquivo: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RestErrorMessage(HttpStatus.BAD_REQUEST, "Falha ao processar o arquivo enviado."));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<RestErrorMessage> dataAccessHandler(DataAccessException ex) {
        logger.error("Erro ao acessar dados: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao acessar dados. Contate o suporte.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(treatedResponse);
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<RestErrorMessage> mailSenderHandler(MailException ex) {
        logger.error("Erro ao enviar email: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao enviar email.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(treatedResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<RestErrorMessage> nullPointerHandler(NullPointerException ex) {
        logger.error("Erro interno inesperado: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado. Contate o suporte.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(treatedResponse);
    }

    @ExceptionHandler(EmptyPathException.class)
    private ResponseEntity<RestErrorMessage> emptyPathHandler(EmptyPathException ex) {
        logger.error("O caminho passado é vazio: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(treatedResponse);
    }
    @ExceptionHandler(InvalidPathException.class)
    private ResponseEntity<RestErrorMessage> invalidPathHandler(InvalidPathException ex) {
        logger.error("O caminho passado é inválido: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(treatedResponse);
    }
    @ExceptionHandler(FlaskConnectionException.class)
    private ResponseEntity<RestErrorMessage> flaskConnectionHandler(FlaskConnectionException ex) {
        logger.error("Erro no microsserviço Flask: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(treatedResponse);
    }
    @ExceptionHandler(BusinessRuleException.class)
        private ResponseEntity<RestErrorMessage> businessRuleHandler(BusinessRuleException ex) {
        logger.error("Erro de regra de negócio: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(treatedResponse);
    }
    @ExceptionHandler(PersistenceException.class)
    private ResponseEntity<RestErrorMessage> persistenceHandler(PersistenceException ex) {
        logger.error("Erro de persistência de dados: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(treatedResponse);
    }
    @ExceptionHandler(ItemNotFoundException.class)
    private ResponseEntity<RestErrorMessage> itemNotFoundHandler(ItemNotFoundException ex) {
        logger.error("Item não pôde ser encontrado: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(treatedResponse);
    }
    @ExceptionHandler(AuthenticationException.class)
    private ResponseEntity<RestErrorMessage> authenticationHandler(AuthenticationException ex) {
        logger.error("Erro de autenticação: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(treatedResponse);
    }
    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<RestErrorMessage> userNotFoundHandler(UserNotFoundException ex) {
        logger.error("Usuário não encontrado: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(treatedResponse);
    }
    @ExceptionHandler(InvalidScoreWeightsException.class)
    private ResponseEntity<RestErrorMessage> invalidScoreWeightHandler(InvalidScoreWeightsException ex) {
        logger.error("O peso definido para a vaga é inválido: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(treatedResponse);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    private ResponseEntity<RestErrorMessage> expiredJwtHandler(ExpiredJwtException ex) {
        logger.error("Token expirado: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(treatedResponse);
    }
    @ExceptionHandler(MalformedJwtException.class)
    private ResponseEntity<RestErrorMessage> malFormedJwtHandler(MalformedJwtException ex) {
        logger.error("Erro ao verificar token: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(treatedResponse);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    private ResponseEntity<RestErrorMessage> unsupportedJwtHandler(UnsupportedJwtException ex) {
        logger.error("Token JWT com formato ou algoritmo não suportado: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(treatedResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<RestErrorMessage> illegalArgumentHandler(IllegalArgumentException ex) {
        logger.error("Token JWT inválido ou ausente: ", ex);
        RestErrorMessage treatedResponse = new RestErrorMessage(HttpStatus.UNAUTHORIZED, "Token JWT inválido ou ausente.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(treatedResponse);
    }

}
