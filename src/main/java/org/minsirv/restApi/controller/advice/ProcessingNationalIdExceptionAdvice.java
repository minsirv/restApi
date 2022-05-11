package org.minsirv.restApi.controller.advice;

import org.minsirv.restApi.exceptions.ProcessingNationalIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ProcessingNationalIdExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProcessingNationalIdException.class)
    public ResponseEntity<String> handleNationalIdNotFoundException(ProcessingNationalIdException exc) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exc.getMessage());
    }
}
