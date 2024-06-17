package com.practicum.neuron.entity.table;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserTableSummary {
    // 表 id
    private String id;

    // 表标题
    private String title;

    // 作者
    private String author;

    // 开始时间
    private LocalDateTime beginning;

    // 结束时间
    private LocalDateTime deadline;
}
