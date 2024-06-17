package com.practicum.neuron.entity.answer;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document("answers")
public class Answer {
    // 回答 id
    @Id
    private String id;

    // 表 id
    @Indexed
    @Field("table_id")
    private String tableId;

    // 回答人
    @Indexed
    @Field("respondent")
    private String respondent;

    // 对应问题的数字指纹
    @Indexed
    @Field("fingerprint")
    private String fingerprint;

    // 答案，由于一个字段可能有多个子字段，用一个列表表示
    @Field("answers")
    String[] answers;
}
