package com.practicum.neuron.exception;

public class TableNotExistException extends Exception {
    @Override
    public String getMessage() {
        return "采集表不存在";
    }
}
