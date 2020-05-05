package com.xebia.covidtracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "File format not supported. Please use specified CSV format")
public class InvalidFileException extends RuntimeException {
}
