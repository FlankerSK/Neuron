package com.practicum.neuron.entity.table;

import com.practicum.neuron.entity.QuestionAnswer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AdminTableAnswer {
    // 表标题
    private String title;

    // 回答者
    private String respondent;

    // 提交时间
    private LocalDateTime date;

    // 问题与对应的答案列表
    private List<QuestionAnswer> questionAnswers;
}
