package com.practicum.neuron;

import com.practicum.neuron.entity.question.*;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.mapper.TableMapper;
import com.practicum.neuron.service.DesignService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
public class MongodbTest {
    @Resource
    private DesignService designService;

    @Resource
    private TableMapper tableMapper;

    @Test
    @Order(1)
    void createTableTest() {
        String id = designService.createTable("test", "author");
        log.info(id);
    }

    @Test
    @Order(2)
    void findTableTest() {
        String id = designService.createTable("findTableTest", "author");
        Optional<Table> t = tableMapper.findById(id);
        Assert.isTrue(t.isPresent(), "没有查到表");
    }

    @Test
    @Order(3)
    void updateTableTest() {
        String id = designService.createTable("updateTableTest", "author");
        Question q1 = new FillInQuestion("q1");
        Question q2 = new SingleAnswerMultipleChoiceQuestion(
                "q2",
                Arrays.asList("option1", "option2")
        );
        Question q3 = new MultipleAnswerMultipleChoiceQuestion(
                "q3",
                Arrays.asList("option1", "option2")
        );
        Question q4 = new ListQuestion(
                "q4",
                Arrays.asList("column1", "column2")
        );
        Question q5 = new TableQuestion(
                "q5",
                Arrays.asList("column1", "column2"),
                Arrays.asList("row1", "row2")
        );
        List<Document> questions = Arrays.asList(
                q1.toDocument(),
                q2.toDocument(),
                q3.toDocument(),
                q4.toDocument(),
                q5.toDocument()
        );
        Optional<Table> t = tableMapper.findById(id);
        Assert.isTrue(t.isPresent(), "没有查到表");
        Table table = t.get();
        table.setQuestions(questions);
        tableMapper.save(table);
    }
}
