package com.practicum.neuron.service;

import com.practicum.neuron.entity.FillRule;
import com.practicum.neuron.entity.question.QuestionType;
import com.practicum.neuron.entity.table.AdminTableSummary;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.mapper.ReleaseInfoMapper;
import com.practicum.neuron.mapper.TableMapper;
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
class DesignServiceTest {
    @Resource
    private DesignService designService;

    @Resource
    private ReleaseInfoMapper releaseInfoMapper;

    @Resource
    private TableMapper tableMapper;

    @Test
    @Order(0)
    void setUp() {
        tableMapper.deleteAll();
        releaseInfoMapper.deleteAll();
    }

    @Test
    @Order(1)
    void createTable() {
        String id = designService.createTable("createTable-test", "test");
        log.info(id);
    }

    @Test
    @Order(2)
    void updateQuestion() {
        String id = designService.createTable("updateTable-test", "test");
        String title = "updateTable-test-update";
        // 填空
        Document fillInQuestion = new Document();
        fillInQuestion.put("type", QuestionType.FILL_IN);
        fillInQuestion.put("title", "q1");

        // 单选
        Document singleAnswerMultipleChoiceQuestion = new Document();
        singleAnswerMultipleChoiceQuestion.put("type", QuestionType.SINGLE_ANSWER_MULTIPLE_CHOICE);
        singleAnswerMultipleChoiceQuestion.put("title", "q2");
        singleAnswerMultipleChoiceQuestion.put("options", Arrays.asList("option1", "option2"));

        // 多选
        Document multipleAnswerMultipleChoiceQuestion = new Document();
        multipleAnswerMultipleChoiceQuestion.put("type", QuestionType.MULTIPLE_ANSWER_MULTIPLE_CHOICE);
        multipleAnswerMultipleChoiceQuestion.put("title", "q3");
        multipleAnswerMultipleChoiceQuestion.put("options", Arrays.asList("option1", "option2"));

        // 列表
        Document listQuestion = new Document();
        listQuestion.put("type", QuestionType.LIST_QUESTION);
        listQuestion.put("title", "q4");
        listQuestion.put("row_heading_list", Arrays.asList("row1", "row2"));

        // 表格
        Document tableQuestion = new Document();
        tableQuestion.put("type", QuestionType.TABLE_QUESTION);
        tableQuestion.put("title", "q5");
        tableQuestion.put("row_heading_list", Arrays.asList("row1", "row2"));
        tableQuestion.put("column_heading_list", Arrays.asList("column1", "column2"));


        List<Document> questions = Arrays.asList(
                fillInQuestion,
                singleAnswerMultipleChoiceQuestion,
                multipleAnswerMultipleChoiceQuestion,
                listQuestion,
                tableQuestion
        );
        try {
            designService.updateQuestion(id, title, questions);
        }
        catch (Exception e) {
            Assert.isTrue(false, e.getMessage());
        }

    }

    @Test
    @Order(3)
    void releaseTable() {
        String id = designService.createTable("releaseTable-test", "test");
        LocalDateTime beginning = LocalDateTime.parse("2024-06-08T00:00:00");
        LocalDateTime deadline = LocalDateTime.parse("9999-12-30T23:00:00");
        FillRule rule = new FillRule();
        try {
            designService.releaseTable(id, beginning, deadline, rule);
        }
        catch (Exception e) {
            Assert.isTrue(false, e.getMessage());
        }
    }

    @Test
    @Order(4)
    void stopRelease() {
        String id = designService.createTable("stopRelease-test", "test");
        LocalDateTime beginning = LocalDateTime.parse("2024-06-08T00:00:00");
        LocalDateTime deadline = LocalDateTime.parse("9999-12-30T23:59:59");
        FillRule rule = new FillRule();
        try {
            designService.releaseTable(id, beginning, deadline, rule);
            designService.stopRelease(id);
        }
        catch (Exception e) {
            Assert.isTrue(false, e.getMessage());
        }
    }

    @Test
    @Order(5)
    void deleteTable() {
    }

    @Test
    @Order(6)
    void getTableSummary() {
        String id = designService.createTable("getTableSummary-test", "test");
        try {
            List<AdminTableSummary> summaryList = designService.getTableSummary("test");
            for (AdminTableSummary summary : summaryList) {
                log.info(summary.toString());
            }
        }
        catch (Exception e) {
            Assert.isTrue(false, e.getMessage());
        }
    }

    @Test
    @Order(7)
    void getTable() {
        String id = designService.createTable("getTable-test", "test");
        try {
            Table table = designService.getTable(id);
            log.info(table.toString());
        }
        catch (Exception e) {
            Assert.isTrue(false, e.getMessage());
        }
    }
}