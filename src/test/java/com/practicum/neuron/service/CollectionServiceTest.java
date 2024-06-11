package com.practicum.neuron.service;

import com.practicum.neuron.entity.QuestionType;
import com.practicum.neuron.entity.answer.Answer;
import com.practicum.neuron.entity.answer.AnswerSummary;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.entity.table.TableAnswer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CollectionServiceTest {
    @Resource
    private DesignService designService;

    @Resource
    private FillService fillService;

    @Resource
    private CollectionService collectionService;

    @Test
    @Order(1)
    void getAnswerList() {
        String id = designService.createTable("getAnswerList-test", "test1");
        // 填空
        Document fillInQuestion = new Document();
        fillInQuestion.put("type", QuestionType.FILL_IN.name());
        fillInQuestion.put("title", "q1");

        // 单选
        Document singleAnswerMultipleChoiceQuestion = new Document();
        singleAnswerMultipleChoiceQuestion.put("type", QuestionType.SINGLE_ANSWER_MULTIPLE_CHOICE.name());
        singleAnswerMultipleChoiceQuestion.put("title", "q2");
        singleAnswerMultipleChoiceQuestion.put("options", Arrays.asList("option1", "option2"));

        List<Document> questions = Arrays.asList(
                fillInQuestion,
                singleAnswerMultipleChoiceQuestion
        );
        try {
            designService.updateQuestion(id, "getAnswerList-test", questions);
            Table table = designService.getTable(id);
            List<Document> saveQuestions = table.getQuestions();
            String f1 = saveQuestions.get(0).getString("fingerprint");
            String f2 = saveQuestions.get(1).getString("fingerprint");
            Answer a1 = Answer.builder()
                    .tableId(id)
                    .respondent("test2")
                    .fingerprint(f1)
                    .answers(new String[]{"answer"})
                    .build();
            Answer a2 = Answer.builder()
                    .tableId(id)
                    .respondent("test2")
                    .fingerprint(f2)
                    .answers(new String[]{"option1"})
                    .build();
            fillService.saveAnswer(id, "test2", List.of(a1, a2));
            fillService.submitAnswer(id, "test2", LocalDateTime.now());
            List<AnswerSummary> answers = collectionService.getAnswerList(id);
            for(AnswerSummary answerSummary : answers) {
                log.info(answerSummary.toString());
            }
        }
        catch (Exception e) {
            Assert.isTrue(false, e.getMessage());
        }
    }

    @Test
    @Order(2)
    void getOneTableUserAnswer() {
        String id = designService.createTable("getOneTableUserAnswer-test", "test1");
        // 填空
        Document fillInQuestion = new Document();
        fillInQuestion.put("type", QuestionType.FILL_IN.name());
        fillInQuestion.put("title", "q1");

        // 单选
        Document singleAnswerMultipleChoiceQuestion = new Document();
        singleAnswerMultipleChoiceQuestion.put("type", QuestionType.SINGLE_ANSWER_MULTIPLE_CHOICE.name());
        singleAnswerMultipleChoiceQuestion.put("title", "q2");
        singleAnswerMultipleChoiceQuestion.put("options", Arrays.asList("option1", "option2"));

        List<Document> questions = Arrays.asList(
                fillInQuestion,
                singleAnswerMultipleChoiceQuestion
        );
        try {
            designService.updateQuestion(id, "getAnswerList-test", questions);
            Table table = designService.getTable(id);
            List<Document> saveQuestions = table.getQuestions();
            String f1 = saveQuestions.get(0).getString("fingerprint");
            String f2 = saveQuestions.get(1).getString("fingerprint");
            Answer a1 = Answer.builder()
                    .tableId(id)
                    .respondent("test2")
                    .fingerprint(f1)
                    .answers(new String[]{"answer"})
                    .build();
            Answer a2 = Answer.builder()
                    .tableId(id)
                    .respondent("test2")
                    .fingerprint(f2)
                    .answers(new String[]{"option1"})
                    .build();
            fillService.saveAnswer(id, "test2", List.of(a1, a2));
            fillService.submitAnswer(id, "test2", LocalDateTime.now());
            List<AnswerSummary> answers = collectionService.getAnswerList(id);
            String answerId = answers.get(0).getAnswerId();
            TableAnswer tableAnswer =  collectionService.getOneTableUserAnswer(id, answerId);
            log.info(tableAnswer.toString());
        }
        catch (Exception e) {
            Assert.isTrue(false, e.getMessage());
        }
    }
}