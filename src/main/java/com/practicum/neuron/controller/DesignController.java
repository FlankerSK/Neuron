package com.practicum.neuron.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practicum.neuron.entity.response.ResponseBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.exception.TableNotExistException;
import com.practicum.neuron.service.DesignService;
import com.practicum.neuron.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.ArrayList;


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

    @GetMapping("/api/admin/test")
    public ResponseEntity<ResponseBody> admin() {
        return new ResponseEntity<>(
                new ResponseBody(Status.SUCCESS, "请求的的资源"),
                HttpStatus.OK
        );
    }

    @PostMapping("${api.admin.table.create}")
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

    @PostMapping("${api.admin.question.update}")
    public ResponseEntity<ResponseBody> updateQuestion(HttpServletRequest request)
            throws IOException {
        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
        String id = jsonNode.get("id").asText();
        JsonNode questionListNode = jsonNode.get("questions");
        ArrayList<Document> questions = new ArrayList<>();
        for (JsonNode questionNode : questionListNode) {
            Document question = objectMapper.treeToValue(questionNode, Document.class);
            questions.add(question);
        }
        try{
            designService.updateQuestion(id, questions);
            return new ResponseEntity<>(
                    new ResponseBody(Status.SUCCESS),
                    HttpStatus.OK
            );
        }
        catch(TableNotExistException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_NOT_EXIST),
                    HttpStatus.BAD_REQUEST
            );
        }
        catch(Exception e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_UNKNOWN_ERROR),
                   HttpStatus.BAD_REQUEST
            );
        }
    }
}
