package com.practicum.neuron.controller;

import com.practicum.neuron.entity.answer.AnswerSummary;
import com.practicum.neuron.entity.response.ResponseBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.entity.table.AdminTableAnswer;
import com.practicum.neuron.service.CollectionService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class CollectionController {
    @Resource
    private CollectionService collectionService;

    @SneakyThrows
    @GetMapping("/table/{id}/answer")
    public ResponseBody getAnswers(@PathVariable String id) {
        List<AnswerSummary> list = collectionService.getAnswerList(id);
        return new ResponseBody(Status.SUCCESS, list);
    }

    @SneakyThrows
    @GetMapping("/table/{tableId}/answer/{AnswerId}")
    public ResponseBody getAnswers(@PathVariable String tableId, @PathVariable String AnswerId) {
        AdminTableAnswer tableAnswer = collectionService.getOneTableUserAnswer(tableId, AnswerId);
        return new ResponseBody(Status.SUCCESS, tableAnswer);
    }
}
