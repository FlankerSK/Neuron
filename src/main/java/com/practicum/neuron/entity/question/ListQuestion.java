package com.practicum.neuron.entity.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class ListQuestion extends Question {
    @Field("column_heading_list")
    List<String> columnHeadingList;

    public ListQuestion() {
        super(QuestionType.LIST_QUESTION);
        columnHeadingList = new ArrayList<String>();
    }

    public ListQuestion(String tittle, List<String> columnHeadingList) {
        super(QuestionType.LIST_QUESTION, tittle);
        this.columnHeadingList = columnHeadingList;
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();
        doc.append("type", getType());
        doc.append("title", getTitle());
        doc.append("columnHeadingList", columnHeadingList);
        return doc;
    }
}
