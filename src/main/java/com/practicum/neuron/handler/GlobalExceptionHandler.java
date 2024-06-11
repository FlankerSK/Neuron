package com.practicum.neuron.handler;

import com.practicum.neuron.entity.response.RespondBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<RespondBody> handleNotFoundException() {
        RespondBody body = new RespondBody(Status.ACCESS_NOT_FOUND);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<RespondBody> handleMethodNotAllowedException() {
        RespondBody body = new RespondBody(Status.ACCESS_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<RespondBody> handleUserExistException() {
        return new ResponseEntity<>(new RespondBody(Status.REGISTER_USER_EXIST), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TableNotExistException.class)
    public ResponseEntity<RespondBody> handleTableNotExistException() {
        RespondBody body = new RespondBody(Status.TABLE_NOT_EXIST);
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TableAlreadyPublishedException.class)
    public ResponseEntity<RespondBody> handleTableAlreadyPublishedException() {
        RespondBody body = new RespondBody(Status.TABLE_ALREADY_PUBLISHED);
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TableAlreadyEndException.class)
    public ResponseEntity<RespondBody> handleTableAlreadyEndException() {
        RespondBody body = new RespondBody(Status.TABLE_ALREADY_END);
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({DateTimeParseException.class, InvalidDateException.class})
    public ResponseEntity<RespondBody> handleDateTimeParseException() {
        RespondBody body = new RespondBody(Status.TABLE_INVALID_TIME);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TableUnpublishException.class)
    public ResponseEntity<RespondBody> handleTableUnpublishException() {
        RespondBody body = new RespondBody(Status.TABLE_UNPUBLISHED);
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<RespondBody> handleNullPointerException() {
        RespondBody body = new RespondBody(Status.ACCESS_INVALID_PARAMETER);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
