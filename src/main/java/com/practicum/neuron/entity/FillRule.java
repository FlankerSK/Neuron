package com.practicum.neuron.entity;

import com.practicum.neuron.entity.account.UserInfo;
import lombok.Data;

@Data
public class FillRule {
    public boolean canFill(UserInfo userInfo) {
        return true;
    }
}
