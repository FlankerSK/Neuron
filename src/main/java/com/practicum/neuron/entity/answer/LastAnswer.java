package com.practicum.neuron.entity.answer;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("last_answers")
@CompoundIndex(name = "index", def = "{'fingerprint': 1, 'respondent': 1}")
public class LastAnswer {
    @Id
    private String id;

    // 对应问题的数字指纹
    @Field("fingerprint")
    private String fingerprint;

    // 回答人
    @Field("respondent")
    private String respondent;

    // 答案，由于一个字段可能有多个子字段，用一个列表表示
    @Field("answers")
    String[] answers;

    public LastAnswer(String fingerprint, String respondent, String[] answers) {
        this.id = fingerprint + respondent; // 将问题的数字指纹与回答者的字符串合并作为id
        this.fingerprint = fingerprint;
        this.respondent = respondent;
        this.answers = answers;
    }
}
