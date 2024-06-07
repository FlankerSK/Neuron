package com.practicum.neuron.entity.table;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminTableSummary {
    // 表 id
    private String id;

    // 表标题
    private String tittle;

    // 更新时间，默认为创建时间
    private LocalDateTime updateDate;

    // 表状态
    private TableStatus status;
}
