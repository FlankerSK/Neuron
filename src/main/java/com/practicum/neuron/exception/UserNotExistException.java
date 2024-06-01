package com.practicum.neuron.exception;

public class UserNotExistException extends Exception {
    @Override
    public String getMessage() {
        return "User not exist";
    }
}
