package org.minsirv.restApi.controller.advice;

import org.minsirv.restApi.exceptions.NationalIdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class NationalIdNotFoundExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NationalIdNotFoundException.class)
    public ResponseEntity<String> handleNationalIdNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
