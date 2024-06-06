package com.practicum.neuron.entity.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class TableQuestion extends Question {
    @Field("column_heading_list")
    List<String> columnHeadingList;

    @Field("row_heading_list")
    List<String> rowHeadingList;

    public TableQuestion() {
        super(QuestionType.TABLE_QUESTION);
        this.columnHeadingList = new ArrayList<String>();
        this.rowHeadingList = new ArrayList<>();
    }

    public TableQuestion(
            String tittle,
            List<String> columnHeadingList,
            List<String> rowHeadingList
    ) {
        super(QuestionType.TABLE_QUESTION, tittle);
        this.columnHeadingList = columnHeadingList;
        this.rowHeadingList = rowHeadingList;

    }

    @Override
    public Document toDocument() {
        Document doc = new Document();
        doc.append("type", getType());
        doc.append("title", getTitle());
        doc.append("column_heading_list", columnHeadingList);
        doc.append("row_heading_list", rowHeadingList);
        return doc;
    }
}
