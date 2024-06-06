package com.practicum.neuron.entity.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.Document;


@EqualsAndHashCode(callSuper = false)
@Data
public class FillInQuestion extends Question {
    public FillInQuestion() {
        super(QuestionType.FILL_IN);
    }

    public FillInQuestion(String tittle) {
        super(QuestionType.FILL_IN, tittle);
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();
        doc.append("type", getType());
        doc.append("title", getTitle());
        return doc;
    }
}


