package com.practicum.neuron.entity.question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.bson.Document;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = FillInQuestion.class,
                name = "FILL_IN"
        ),
        @JsonSubTypes.Type(
                value = FileUploadQuestion.class,
                name = "FILE_UPLOAD"
        ),
        @JsonSubTypes.Type(
                value = SingleAnswerMultipleChoiceQuestion.class,
                name = "SINGLE_ANSWER_MULTIPLE_CHOICE"
        ),
        @JsonSubTypes.Type(
                value = MultipleAnswerMultipleChoiceQuestion.class,
                name = "MULTIPLE_ANSWER_MULTIPLE_CHOICE"
        ),
        @JsonSubTypes.Type(
                value = ListQuestion.class,
                name = "LIST_QUESTION"
        ),
        @JsonSubTypes.Type(
                value = TableQuestion.class,
                name = "TABLE_QUESTION"
        )
})
@Data
public abstract class Question {
    // 问题类型
    private final QuestionType type;

    protected String title;

    public Question() {
        this.type = null;
        this.title = "";
    }

    public Question(QuestionType type) {
        this.type = type;
        this.title = "";
    }

    public Question(QuestionType type, String title) {
        this.type = type;
        this.title = title;
    }

    public abstract Document toDocument();
}
