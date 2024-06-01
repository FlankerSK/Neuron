package com.practicum.neuron.entity;


import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

/**
 * 安全绑定信息 DTO.
 */
@Data
@Builder
public class SecurityInfoDto {
    @Default
    private String email = "";

    @Default
    private String phone = "";
}
