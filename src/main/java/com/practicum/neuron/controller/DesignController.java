package com.practicum.neuron.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practicum.neuron.entity.FillRule;
import com.practicum.neuron.entity.response.RespondBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.entity.table.AdminTableSummary;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.exception.InvalidDateException;
import com.practicum.neuron.service.DesignService;
import com.practicum.neuron.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 采集表设计模块控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
public class DesignController {
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private DesignService designService;

    @SneakyThrows
    @PostMapping("/table")
    public RespondBody createTable(HttpServletRequest request) {
        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
        String tittle = jsonNode.get("title").asText();
        String token = jwtUtil.getToken(request);
        String author = jwtUtil.getUserNameFromToken(token);
        String id = designService.createTable(tittle, author);
        return new RespondBody(Status.SUCCESS, id);
    }

    @SneakyThrows
    @PutMapping("/table/{id}")
    public RespondBody updateTable(@PathVariable String id, HttpServletRequest request) {
        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
        String title = jsonNode.get("title").asText();
        JsonNode questionListNode = jsonNode.get("questions");

        ArrayList<Document> questions = new ArrayList<>();
        for (JsonNode questionNode : questionListNode) {
            Document question = objectMapper.treeToValue(questionNode, Document.class);
            questions.add(question);
        }
        designService.updateQuestion(id, title, questions);
        return new RespondBody(Status.SUCCESS);
    }

    @SneakyThrows
    @PostMapping("/table/{id}/release")
    public RespondBody releaseTable(@PathVariable String id, HttpServletRequest request) {
        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
        LocalDateTime beginning = LocalDateTime.parse(jsonNode.get("beginning").asText());
        LocalDateTime deadline = LocalDateTime.parse(jsonNode.get("deadline").asText());
        FillRule rule = objectMapper.treeToValue(jsonNode.get("fill_rule"), FillRule.class);
        if (beginning.isBefore(deadline)) {
            designService.releaseTable(id, beginning, deadline, rule);
            return new RespondBody(Status.SUCCESS);
        }
        else {
            throw new InvalidDateException();
        }
    }

    @SneakyThrows
    @DeleteMapping("/table/{id}/release")
    public ResponseEntity<RespondBody> stopReleaseTable(@PathVariable String id) {
        designService.stopRelease(id);
        return new ResponseEntity<>(new RespondBody(Status.SUCCESS), HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("/table")
    public RespondBody getTableSummary(HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        String username = jwtUtil.getUserNameFromToken(token);
        List<AdminTableSummary> summaryList = designService.getTableSummary(username);
        return new RespondBody(Status.SUCCESS, summaryList);
    }

    @SneakyThrows
    @GetMapping("/table/{id}")
    public RespondBody getTable(@PathVariable String id) {
        Table table = designService.getTable(id);
        return new RespondBody(Status.SUCCESS, table);
    }

    @SneakyThrows
    @DeleteMapping("/table/{id}")
    public RespondBody deleteTable(@PathVariable String id) {
        designService.deleteTable(id);
        return new RespondBody(Status.SUCCESS);
    }
}
