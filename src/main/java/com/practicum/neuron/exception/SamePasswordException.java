package com.practicum.neuron.exception;

public class SamePasswordException extends Exception {
    @Override
    public String getMessage() {
        return "The old password is the same as the new password";
    }
}
