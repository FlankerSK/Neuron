package com.practicum.neuron.exception;

public class TableUnpublishException extends Exception {
    @Override
    public String getMessage() {
        return "采集表未发布";
    }
}
