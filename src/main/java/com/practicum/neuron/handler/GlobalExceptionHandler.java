package com.practicum.neuron.handler;

import com.practicum.neuron.entity.response.ResponseBody;
import com.practicum.neuron.entity.response.Status;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoResourceFoundException.class)
    @org.springframework.web.bind.annotation.ResponseBody
    public ResponseEntity<ResponseBody> handleNoResourceFoundException(NoResourceFoundException e) {
        ResponseBody body = new ResponseBody(Status.ACCESS_NOT_FOUND);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(body, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @org.springframework.web.bind.annotation.ResponseBody
    public ResponseEntity<ResponseBody> handleNoHandlerFoundException(NoHandlerFoundException e) {
        ResponseBody body = new ResponseBody(Status.ACCESS_NOT_FOUND);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new org.springframework.http.ResponseEntity<>(body, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @org.springframework.web.bind.annotation.ResponseBody
    public ResponseEntity<ResponseBody> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ResponseBody body = new ResponseBody(Status.ACCESS_METHOD_NOT_ALLOWED);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new org.springframework.http.ResponseEntity<>(body, headers, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
