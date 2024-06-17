package com.practicum.neuron.handler;

import com.practicum.neuron.entity.response.ResponseBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<ResponseBody> handleNotFoundException() {
        ResponseBody body = new ResponseBody(Status.ACCESS_NOT_FOUND);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseBody> handleMethodNotAllowedException() {
        ResponseBody body = new ResponseBody(Status.ACCESS_METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ResponseBody> handleUserExistException() {
        return new ResponseEntity<>(new ResponseBody(Status.REGISTER_USER_EXIST), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TableNotExistException.class)
    public ResponseEntity<ResponseBody> handleTableNotExistException() {
        ResponseBody body = new ResponseBody(Status.TABLE_NOT_EXIST);
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TableAlreadyPublishedException.class)
    public ResponseEntity<ResponseBody> handleTableAlreadyPublishedException() {
        ResponseBody body = new ResponseBody(Status.TABLE_ALREADY_PUBLISHED);
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TableAlreadyEndException.class)
    public ResponseEntity<ResponseBody> handleTableAlreadyEndException() {
        ResponseBody body = new ResponseBody(Status.TABLE_ALREADY_END);
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({DateTimeParseException.class, InvalidDateException.class})
    public ResponseEntity<ResponseBody> handleDateTimeParseException() {
        ResponseBody body = new ResponseBody(Status.TABLE_INVALID_TIME);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TableUnpublishException.class)
    public ResponseEntity<ResponseBody> handleTableUnpublishException() {
        ResponseBody body = new ResponseBody(Status.TABLE_UNPUBLISHED);
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({NullPointerException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ResponseBody> handleNullPointerException() {
        ResponseBody body = new ResponseBody(Status.ACCESS_INVALID_PARAMETER);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AnswerNotExistException.class)
    public ResponseEntity<ResponseBody> handleAnswerNotExistException() {
        ResponseBody body = new ResponseBody(Status.DATA_NOT_EXIST);
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
}
