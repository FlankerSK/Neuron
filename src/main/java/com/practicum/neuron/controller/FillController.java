package com.practicum.neuron.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practicum.neuron.entity.answer.Answer;
import com.practicum.neuron.entity.response.RespondBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.entity.table.TableStatus;
import com.practicum.neuron.entity.table.UserTableSummary;
import com.practicum.neuron.exception.TableAlreadyEndException;
import com.practicum.neuron.exception.TableUnpublishException;
import com.practicum.neuron.service.FillService;
import com.practicum.neuron.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class FillController {
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private FillService fillService;

    @SneakyThrows
    @GetMapping("/table")
    public ResponseEntity<RespondBody> getTableSummary(HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        String username = jwtUtil.getUserNameFromToken(token);
        List<UserTableSummary> summaryList = fillService.getTableSummary(username);
        return new ResponseEntity<>(
                new RespondBody(Status.SUCCESS, summaryList),
                HttpStatus.OK
        );
    }

    @SneakyThrows
    @GetMapping("/table/{id}")
    public RespondBody getTable(@PathVariable String id) {
        Table table = fillService.getTable(id);
        TableStatus status = table.getStatus();
        switch (status) {
            case UNPUBLISH -> throw new TableUnpublishException();
            case END -> throw new TableAlreadyEndException();
        }
        return new RespondBody(Status.SUCCESS, table);
    }

    @SneakyThrows
    @PutMapping("/table/{id}/answer")
    public RespondBody saveAnswer(@PathVariable String id, HttpServletRequest request) {
        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
        String token = jwtUtil.getToken(request);
        String respondent = jwtUtil.getUserNameFromToken(token);
        JsonNode answerListNode = jsonNode.get("answers");
        List<Answer> answerList = new ArrayList<>();
        for (JsonNode answerNode : answerListNode) {
            String fingerprint = answerNode.get("fingerprint").asText();
            String[] answers = objectMapper.treeToValue(answerNode.get("answers"), String[].class);
            answerList.add(Answer.builder()
                    .tableId(id)
                    .respondent(respondent)
                    .fingerprint(fingerprint)
                    .answers(answers)
                    .build()
            );
        }
        fillService.saveAnswer(id, respondent, answerList);
        return new RespondBody(Status.SUCCESS);
    }

    @SneakyThrows
    @PutMapping("/table/{id}/answer/submit")
    public RespondBody submitAnswer(@PathVariable String id, HttpServletRequest request) {
        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
        String token = jwtUtil.getToken(request);
        String respondent = jwtUtil.getUserNameFromToken(token);
        LocalDateTime date = LocalDateTime.parse(jsonNode.get("date").asText());
        fillService.submitAnswer(id, respondent, date);
        return new RespondBody(Status.SUCCESS);
    }
}
