package com.practicum.neuron.entity.table;

import com.practicum.neuron.entity.QuestionAnswer;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserTableAnswer {
    // 表标题
    private String title;

    // 问题与对应的答案列表
    private List<QuestionAnswer> questionAnswers;
}
