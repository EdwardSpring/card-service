package com.mintyn.cardservice.exception;

import java.sql.SQLIntegrityConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.mintyn.cardservice.model.ApiResponse;

@RestControllerAdvice
public class RuntimeExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<?> handleBadRequest(RuntimeException e) {
        log.error("{}BAD REQUEST{}", "-".repeat(10).intern(), "-".repeat(10).intern());
        e.printStackTrace();
        ApiResponse<?> response = new ApiResponse<>(false, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("{}BAD REQUEST{}", "-".repeat(10).intern(), "-".repeat(10).intern());
        e.printStackTrace();
        String msg = String.format("Invalid value '%s' for field '%s'", e.getValue(), e.getName());
        return ResponseEntity.status(400).body(new ApiResponse<>(false, msg));
    }
 
    @ExceptionHandler(value = { InvalidFormatException.class })
    protected ResponseEntity<?> handleHttpMessageNotReadableException(InvalidFormatException e) {
        log.error("{}BAD REQUEST{}", "-".repeat(10).intern(), "-".repeat(10).intern());
        e.printStackTrace();
        return ResponseEntity.status(400).body(new ApiResponse<>(false, e.getOriginalMessage()));
    }

    @ExceptionHandler(value = { AccountNotFoundException.class })
    protected ResponseEntity<?> handleAccountNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "An error occured"));
    }

    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class, })
    protected ResponseEntity<?> handleIntegrityException(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new ApiResponse<>(false, e.getMessage()));
    }

    @ExceptionHandler(value = {WebClientResponseException.class, })
    protected ResponseEntity<?> handleIntegrityException(WebClientResponseException e) {
        // e.getst
        e.printStackTrace();
        return ResponseEntity.status(e.getStatusCode()).body(new ApiResponse<>(false, e.getMessage()));
    }



    
}
