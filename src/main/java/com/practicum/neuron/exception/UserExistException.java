package com.practicum.neuron.exception;

public class UserExistException extends Exception {
    @Override
    public String getMessage() {
        return "用户已存在";
    }
}
