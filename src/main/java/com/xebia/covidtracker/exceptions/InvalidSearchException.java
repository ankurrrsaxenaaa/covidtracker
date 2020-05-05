package com.xebia.covidtracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Illegal use of search parameters.")
public class InvalidSearchException extends RuntimeException {
}
