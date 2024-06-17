package com.practicum.neuron.entity.answer;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnswerSummary {
    // 回答 id
    private String answerId;

    // 表 id
    private String tableId;

    // 回答人
    private String respondent;

    // 提交日期
    private LocalDateTime date;
}
