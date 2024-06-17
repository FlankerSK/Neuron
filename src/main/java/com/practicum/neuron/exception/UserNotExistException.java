package com.practicum.neuron.exception;

public class UserNotExistException extends Exception {
    @Override
    public String getMessage() {
        return "用户不存在";
    }
}
