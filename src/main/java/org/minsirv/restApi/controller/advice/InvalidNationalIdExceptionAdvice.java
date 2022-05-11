package org.minsirv.restApi.controller.advice;

import org.minsirv.restApi.exceptions.InvalidNationalIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class InvalidNationalIdExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidNationalIdException.class)
    public ResponseEntity<String> handleMaxSizeException(InvalidNationalIdException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(exc.getMessage());
    }
}
