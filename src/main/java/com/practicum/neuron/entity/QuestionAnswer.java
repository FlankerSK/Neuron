package com.practicum.neuron.entity;

import lombok.Data;
import org.bson.Document;

@Data
public class QuestionAnswer {
    private Document question;
    private String[] answers;

    public QuestionAnswer(Document question, String[] answers) {
        this.question = question;
        this.answers = answers;
    }
}
