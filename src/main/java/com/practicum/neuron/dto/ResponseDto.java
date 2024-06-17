package com.practicum.neuron.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

import java.io.Serializable;


/**
 * 后端响应体
 *
 *
 */
@Data
@Builder
public class ResponseDto implements Serializable {
    private static final ObjectMapper mapper = new ObjectMapper();

    // 自定义响应码
    @Default
    private int code = 0;

    // 响应信息
    @Default
    private String message = "";

    // 数据
    @Default
    private Object data = null;

    public ResponseDto(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseDto(Status status, Object data) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = data;
    }

    /**
     * 根据 响应状态 Status 构造响应体
     *
     * @param status 响应状态
     * @see Status
     */
    public ResponseDto(Status status) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = null;
    }

    public ResponseDto setCode(int code) {
        this.code = code;
        return this;
    }

    public ResponseDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public ResponseDto setData(Object data) {
        this.data = data;
        return this;
    }

    /**
     * 生成 Json 字符串
     *
     * @return Json 字符串
     * @throws JsonProcessingException JSON处理过程中出现错误
     */
    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
