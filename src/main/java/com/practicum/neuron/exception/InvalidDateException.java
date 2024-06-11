package com.practicum.neuron.exception;

public class InvalidDateException extends Exception {
    @Override
    public String getMessage() {
        return "无效的时间参数";
    }
}
