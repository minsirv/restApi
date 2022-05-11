package org.minsirv.restApi.controller.advice;

import org.minsirv.restApi.exceptions.ReadFileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ReadFileExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ReadFileException.class)
    public ResponseEntity<String> handleNationalIdNotFoundException(ReadFileException exc) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exc.getMessage());
    }
}
