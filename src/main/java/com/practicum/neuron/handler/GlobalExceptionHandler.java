package com.practicum.neuron.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.practicum.neuron.entity.ResponseBody;
import com.practicum.neuron.entity.Status;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoResourceFoundException.class)
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<String> handleNoResourceFoundException(NoResourceFoundException e)
            throws JsonProcessingException {
        String body = new ResponseBody(Status.ACCESS_NOT_FOUND).toJson();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new org.springframework.http.ResponseEntity<>(body, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException e)
            throws JsonProcessingException {
        String body = new ResponseBody(Status.ACCESS_NOT_FOUND).toJson();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new org.springframework.http.ResponseEntity<>(body, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<String> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e
    ) throws JsonProcessingException {
        String body = new ResponseBody(Status.ACCESS_METHOD_NOT_ALLOWED).toJson();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new org.springframework.http.ResponseEntity<>(body, headers, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
