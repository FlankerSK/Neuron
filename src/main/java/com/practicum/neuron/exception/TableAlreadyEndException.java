package com.practicum.neuron.exception;

public class TableAlreadyEndException extends Exception {
    @Override
    public String getMessage() {
        return "数据采集表已结束收集";
    }
}
