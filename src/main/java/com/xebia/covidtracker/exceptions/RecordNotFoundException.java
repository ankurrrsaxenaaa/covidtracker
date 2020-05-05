package com.xebia.covidtracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No record found for selected query.")
public class RecordNotFoundException extends RuntimeException {
}
