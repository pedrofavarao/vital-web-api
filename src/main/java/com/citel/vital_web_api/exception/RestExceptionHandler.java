package com.citel.vital_web_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLSyntaxErrorException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<RestErrorMessage> methodArgumentTypeException
            (MethodArgumentTypeMismatchException exception){
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST,
                exception.getName(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    private ResponseEntity<RestErrorMessage> sqlSyntaxException(
            SQLSyntaxErrorException exception){
        RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                "SQL Error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

}
