package com.practicum.neuron.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practicum.neuron.entity.FillRule;
import com.practicum.neuron.entity.response.ResponseBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.entity.table.AdminTableSummary;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.exception.TableAlreadyEndException;
import com.practicum.neuron.exception.TableAlreadyPublishedException;
import com.practicum.neuron.exception.TableNotExistException;
import com.practicum.neuron.exception.TableUnpublishException;
import com.practicum.neuron.service.DesignService;
import com.practicum.neuron.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * 采集表设计模块控制器
 */
@Slf4j
@Controller
public class DesignController {
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private DesignService designService;

    @PostMapping("/api/admin/table")
    public ResponseEntity<ResponseBody> createTable(HttpServletRequest request)
            throws IOException {
        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
        String tittle = jsonNode.get("title").asText();
        String token = jwtUtil.getToken(request);
        String author = jwtUtil.getUserNameFromToken(token);
        String id = designService.createTable(tittle, author);
        return new ResponseEntity<>(
                new ResponseBody(Status.SUCCESS, id),
                HttpStatus.OK
        );
    }

    @PutMapping("/api/admin/table/{id}")
    public ResponseEntity<ResponseBody> updateTable(@PathVariable String id, HttpServletRequest request)
            throws IOException {
        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
        String title = jsonNode.get("title").asText();
        JsonNode questionListNode = jsonNode.get("questions");
        ArrayList<Document> questions = new ArrayList<>();
        for (JsonNode questionNode : questionListNode) {
            Document question = objectMapper.treeToValue(questionNode, Document.class);
            questions.add(question);
        }
        try{
            designService.updateQuestion(id, title, questions);
            return new ResponseEntity<>(
                    new ResponseBody(Status.SUCCESS),
                    HttpStatus.OK
            );
        }
        catch(TableNotExistException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_NOT_EXIST),
                    HttpStatus.NOT_FOUND
            );
        }
        catch(Exception e) {
            Status status = new Status(
                    Status.TABLE_UNKNOWN_ERROR.getCode(),
                    e.getMessage()
            );
            return new ResponseEntity<>(
                    new ResponseBody(status),
                   HttpStatus.FORBIDDEN
            );
        }
    }

    @PostMapping("/api/admin/table/{id}/release")
    public ResponseEntity<ResponseBody> releaseTable(@PathVariable String id, HttpServletRequest request)
            throws IOException {
        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
        try {
            LocalDateTime beginning = LocalDateTime.parse(jsonNode.get("beginning").asText());
            LocalDateTime deadline = LocalDateTime.parse(jsonNode.get("deadline").asText());
            FillRule rule = objectMapper.treeToValue(jsonNode.get("fill_rule"), FillRule.class);
            if (beginning.isBefore(deadline)) {
                designService.releaseTable(id, beginning, deadline, rule);
                return new ResponseEntity<>(
                        new ResponseBody(Status.SUCCESS),
                        HttpStatus.OK
                );
            }
            else {
                return new ResponseEntity<>(
                        new ResponseBody(Status.TABLE_INVALID_TIME),
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        catch (DateTimeParseException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_INVALID_TIME),
                    HttpStatus.BAD_REQUEST
            );
        }
        catch (NullPointerException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.ACCESS_INVALID_PARAMETER),
                    HttpStatus.BAD_REQUEST
            );
        }
        catch (TableNotExistException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_NOT_EXIST),
                    HttpStatus.NOT_FOUND
            );
        }
        catch (TableAlreadyPublishedException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_ALREADY_PUBLISHED),
                    HttpStatus.FORBIDDEN
            );
        }
        catch (TableAlreadyEndException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_ALREADY_END),
                    HttpStatus.FORBIDDEN
            );
        }
        catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_UNKNOWN_ERROR),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping("/api/admin/table/{id}/release")
    public ResponseEntity<ResponseBody> stopReleaseTable(@PathVariable String id) {
        try {
            designService.stopRelease(id);
            return new ResponseEntity<>(
                    new ResponseBody(Status.SUCCESS),
                    HttpStatus.OK
            );
        }
        catch (DateTimeParseException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_INVALID_TIME),
                    HttpStatus.BAD_REQUEST
            );
        }
        catch (NullPointerException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.ACCESS_INVALID_PARAMETER),
                    HttpStatus.BAD_REQUEST
            );
        }
        catch (TableNotExistException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_NOT_EXIST),
                    HttpStatus.FORBIDDEN
            );
        }
        catch (TableUnpublishException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_UNPUBLISHED),
                    HttpStatus.FORBIDDEN
            );
        }
        catch (TableAlreadyEndException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_ALREADY_END),
                    HttpStatus.FORBIDDEN
            );
        }
        catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_UNKNOWN_ERROR),
                    HttpStatus.FORBIDDEN
            );
        }
    }

    @GetMapping("/api/admin/table")
    public ResponseEntity<ResponseBody> getTableSummary(HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        String username = jwtUtil.getUserNameFromToken(token);
        List<AdminTableSummary> summaryList = designService.getTableSummary(username);
        return new ResponseEntity<>(
                new ResponseBody(Status.SUCCESS, summaryList),
                HttpStatus.OK
        );
    }

    @GetMapping("/api/admin/table/{id}")
    public ResponseEntity<ResponseBody> getTable(@PathVariable String id) {
        try {
            Table table = designService.getTable(id);
            return new ResponseEntity<>(
                    new ResponseBody(Status.SUCCESS, table),
                    HttpStatus.OK
            );
        }
        catch (TableNotExistException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_NOT_EXIST),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}
