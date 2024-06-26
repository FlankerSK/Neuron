package com.practicum.neuron.entity.account;


import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

/**
 * 安全绑定信息
 */
@Data
@Builder
public class SecurityInfo {
    @Default
    private String email = "";

    @Default
    private String phone = "";
}
