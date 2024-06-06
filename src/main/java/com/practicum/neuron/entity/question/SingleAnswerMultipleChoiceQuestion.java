package com.practicum.neuron.entity.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class SingleAnswerMultipleChoiceQuestion extends Question {
    @JsonProperty("options")
    @Field("options")
    List<String> options;

    public SingleAnswerMultipleChoiceQuestion() {
        super(QuestionType.SINGLE_ANSWER_MULTIPLE_CHOICE);
        this.options = new ArrayList<>();
    }

    public SingleAnswerMultipleChoiceQuestion(String tittle, List<String> options) {
        super(QuestionType.SINGLE_ANSWER_MULTIPLE_CHOICE, tittle);
        this.options = options;
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();
        doc.append("type", getType());
        doc.append("title", getTitle());
        doc.append("options", getOptions());
        return doc;
    }
}
