package com.practicum.neuron.exception;

public class AnswerNotExistException extends Exception {
    @Override
    public String getMessage() {
        return "采集数据不存在";
    }
}
