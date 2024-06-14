package com.practicum.neuron.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practicum.neuron.entity.answer.Answer;
import com.practicum.neuron.entity.response.ResponseBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.entity.table.UserTableAnswer;
import com.practicum.neuron.entity.table.UserTableSummary;
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
    public ResponseEntity<ResponseBody> getTableSummary(HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        String username = jwtUtil.getUserNameFromToken(token);
        List<UserTableSummary> summaryList = fillService.getTableSummary(username);
        return new ResponseEntity<>(
                new ResponseBody(Status.SUCCESS, summaryList),
                HttpStatus.OK
        );
    }

    @SneakyThrows
    @PutMapping("/table/{id}/answer")
    public ResponseBody saveAnswer(@PathVariable String id, HttpServletRequest request) {
        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
        String token = jwtUtil.getToken(request);
        String respondent = jwtUtil.getUserNameFromToken(token);
        JsonNode answerListNode = jsonNode.get("answers");
        List<Answer> answerList = new ArrayList<>();
        for (JsonNode answerNode : answerListNode) {
            String fingerprint = answerNode.get("fingerprint").asText();
            String[] answers = objectMapper.treeToValue(answerNode.get("answers"),String[].class);
            answerList.add(Answer.builder()
                    .tableId(id)
                    .respondent(respondent)
                    .fingerprint(fingerprint)
                    .answers(answers)
                    .build()
            );
        }
        fillService.saveAnswer(id, respondent, answerList);
        return new ResponseBody(Status.SUCCESS);
    }

    @SneakyThrows
    @PutMapping("/table/{id}/answer/submit")
    public ResponseBody submitAnswer(@PathVariable String id, HttpServletRequest request) {
        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
        String token = jwtUtil.getToken(request);
        String respondent = jwtUtil.getUserNameFromToken(token);
        LocalDateTime date = LocalDateTime.parse(jsonNode.get("date").asText());
        fillService.submitAnswer(id, respondent, date);
        return new ResponseBody(Status.SUCCESS);
    }

    @SneakyThrows
    @GetMapping("/table/{id}/answer")
    public ResponseBody getTableAnswer(@PathVariable String id, HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        String respondent = jwtUtil.getUserNameFromToken(token);
        UserTableAnswer tableAnswer = fillService.getTableAnswer(id, respondent);
        return new ResponseBody(Status.SUCCESS, tableAnswer);
    }
}
