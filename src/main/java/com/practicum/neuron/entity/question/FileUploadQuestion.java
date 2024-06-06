package com.practicum.neuron.entity.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.Document;

@EqualsAndHashCode(callSuper = false)
@Data
public class FileUploadQuestion extends Question {
    public FileUploadQuestion() {
        super(QuestionType.FILE_UPLOAD);
    }

    public FileUploadQuestion(String tittle) {
        super(QuestionType.FILE_UPLOAD, tittle);
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();
        doc.append("type", getType());
        doc.append("title", getTitle());
        return doc;
    }
}
