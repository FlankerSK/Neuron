package com.practicum.neuron.exception;

public class TableAlreadyPublishedException extends Exception {
    @Override
    public String getMessage() {
        return "数据采集表已发布";
    }
}
