package com.practicum.neuron.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practicum.neuron.entity.Answer;
import com.practicum.neuron.entity.response.ResponseBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.entity.table.TableStatus;
import com.practicum.neuron.entity.table.UserTableSummary;
import com.practicum.neuron.exception.TableNotExistException;
import com.practicum.neuron.service.FillService;
import com.practicum.neuron.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FillController {
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private FillService fillService;

    @GetMapping("/api/user/table")
    public ResponseEntity<ResponseBody> getTableSummary(HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        String username = jwtUtil.getUserNameFromToken(token);
        List<UserTableSummary> summaryList = fillService.getTableSummary(username);
        return new ResponseEntity<>(
                new ResponseBody(Status.SUCCESS, summaryList),
                HttpStatus.OK
        );
    }

    @GetMapping("/api/user/table/{id}")
    public ResponseEntity<ResponseBody> getTable(@PathVariable String id) {
        try {
            Table table = fillService.getTable(id);
            TableStatus status = table.getStatus();
            if(status == TableStatus.UNPUBLISH) {
                return new ResponseEntity<>(
                        new ResponseBody(Status.TABLE_UNPUBLISHED),
                        HttpStatus.FORBIDDEN
                );
            }
            else if (status == TableStatus.PUBLISHING) {
                return new ResponseEntity<>(
                        new ResponseBody(Status.SUCCESS, table),
                        HttpStatus.OK
                );
            }
            else {
                return new ResponseEntity<>(
                        new ResponseBody(Status.TABLE_ALREADY_END, table),
                        HttpStatus.FORBIDDEN
                );
            }
        }
        catch (TableNotExistException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_NOT_EXIST),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PutMapping("/api/user/table/{id}/answer")
    public ResponseEntity<ResponseBody> saveAnswer(@PathVariable String id, HttpServletRequest request) {
        try {
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
            return new ResponseEntity<>(
                    new ResponseBody(Status.SUCCESS),
                    HttpStatus.OK
            );
        }
        catch (NullPointerException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.ACCESS_INVALID_PARAMETER),
                    HttpStatus.BAD_REQUEST
            );
        }
        catch (Exception e) {
            Status status = new Status(Status.TABLE_UNKNOWN_ERROR.getCode(), e.getMessage());
            return new ResponseEntity<>(new ResponseBody(status), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/api/user/table/{id}/answer/submit")
    public ResponseEntity<ResponseBody> submitAnswer(@PathVariable String id, HttpServletRequest request) {
        try {
            JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
            String token = jwtUtil.getToken(request);
            String respondent = jwtUtil.getUserNameFromToken(token);
            LocalDateTime date = LocalDateTime.parse(jsonNode.get("date").asText());
            fillService.submitAnswer(id, respondent, date);
            return new ResponseEntity<>(
                    new ResponseBody(Status.SUCCESS),
                    HttpStatus.OK
            );
        }
        catch (NullPointerException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.ACCESS_INVALID_PARAMETER),
                    HttpStatus.BAD_REQUEST
            );
        }
        catch (DateTimeParseException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_INVALID_TIME),
                    HttpStatus.BAD_REQUEST
            );
        }
        catch (Exception e) {
            Status status = new Status(Status.TABLE_UNKNOWN_ERROR.getCode(), e.getMessage());
            return new ResponseEntity<>(new ResponseBody(status), HttpStatus.FORBIDDEN);
        }
    }
}
