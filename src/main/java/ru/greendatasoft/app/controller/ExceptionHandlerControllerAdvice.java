package ru.greendatasoft.app.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity illegalArgumentException(final Exception ex) {
        log.error(ex.toString(), ex);
        return new ResponseEntity<>(ex.toString(), BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity otherException(final Exception ex) {
        log.error(ex.toString(), ex);
        String msg = ex.getMessage();
        return new ResponseEntity<>(msg == null ? ex.toString() : ex.toString() + "\n" + msg, INTERNAL_SERVER_ERROR);
    }
}