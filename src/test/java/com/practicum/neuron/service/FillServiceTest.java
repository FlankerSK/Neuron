package com.practicum.neuron.service;

import com.practicum.neuron.entity.FillRule;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.entity.table.UserTableSummary;
import com.practicum.neuron.mapper.ReleaseInfoMapper;
import com.practicum.neuron.mapper.TableMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FillServiceTest {
    @Resource
    private DesignService designService;

    @Resource
    private FillService fillService;

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
    @Order(2)
    void getTableSummary() {
        String id1 = designService.createTable("getTableSummary-test-1", "test");
        String id2 = designService.createTable("getTableSummary-test-2", "test");
        LocalDateTime beginning = LocalDateTime.parse("2024-06-08T00:00:00");
        LocalDateTime deadline = LocalDateTime.parse("9999-12-30T23:00:00");
        FillRule rule = new FillRule();
        try {
            designService.releaseTable(id1, beginning, deadline, rule);
            designService.releaseTable(id2, beginning, deadline, rule);
            List<UserTableSummary> summaryList = fillService.getTableSummary("test");
            for (UserTableSummary summary : summaryList) {
                log.info(summary.toString());
            }
        }
        catch (Exception e) {
            Assert.isTrue(false, e.getMessage());
        }
    }

    @Test
    @Order(1)
    void getTable() {
        String id = designService.createTable("getTable-test", "test");
        try {
            Table table = fillService.getTable(id);
            log.info(table.toString());
        }
        catch (Exception e) {
            Assert.isTrue(false, e.getMessage());
        }
    }
}