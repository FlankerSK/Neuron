package com.practicum.neuron.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@Document("submit_info")
public class SubmitInfo {
    @Id
    private String id;

    // 表 id
    @Indexed
    @Field("table_id")
    private String tableId;

    // 回答者
    @Field("respondent")
    private String respondent;

    // 提交日期, 默认为现在
    @Builder.Default
    @Field("date")
    private LocalDateTime date = LocalDateTime.now();
}
