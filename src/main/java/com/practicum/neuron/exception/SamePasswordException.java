package com.practicum.neuron.exception;

public class SamePasswordException extends Exception {
    @Override
    public String getMessage() {
        return "两次密码不一致";
    }
}
