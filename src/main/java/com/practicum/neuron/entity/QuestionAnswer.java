package com.practicum.neuron.entity;

import lombok.Builder;
import lombok.Data;
import org.bson.Document;

@Data
@Builder
public class QuestionAnswer {
    private Document question;
    private String[] answers;

    public QuestionAnswer(Document question, String[] answers) {
        this.question = question;
        this.answers = answers;
    }
}
