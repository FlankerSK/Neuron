package com.practicum.neuron.entity;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@Document("release_info")
public class ReleaseInfo {
    // 表 id
    @Indexed
    @Field("table_id")
    private String tableId;

    // 标题
    @Field("title")
    private String title;

    // 作者
    @Field("author")
    private String author;

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
