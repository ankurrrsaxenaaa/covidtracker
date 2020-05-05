package com.xebia.covidtracker.exceptions;

import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(CsvRequiredFieldEmptyException.class)
    ResponseEntity<?> csvEmptyFieldException(CsvRequiredFieldEmptyException e) {
        return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
    }
}
