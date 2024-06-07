package com.practicum.neuron.entity;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@Document("release_info")
public class ReleaseInfo {
    @Id
    private String id;

    // 表 tableId
    @Indexed
    @Field("table_id")
    private String tableId;

    // 开始日期, 默认为现在
    @Default
    @Field("beginning")
    private LocalDateTime beginning = LocalDateTime.now();

    // 结束日期, 默认为最大值，即没有结束日期
    @Default
    @Field("deadline")
    private LocalDateTime deadline = LocalDateTime.MIN;

    // 填写规则, 默认为无
    @Field("fill_rule")
    private FillRule fillRule;
}
