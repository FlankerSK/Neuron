package com.practicum.neuron.controller;

import com.practicum.neuron.entity.answer.AnswerSummary;
import com.practicum.neuron.entity.response.ResponseBody;
import com.practicum.neuron.entity.response.Status;
import com.practicum.neuron.entity.table.TableAnswer;
import com.practicum.neuron.exception.AnswerNotExistException;
import com.practicum.neuron.exception.TableNotExistException;
import com.practicum.neuron.service.CollectionService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/admin")
public class CollectionController {
    @Resource
    private CollectionService collectionService;

    @GetMapping("/table/{id}/answer")
    public ResponseEntity<ResponseBody> getAnswers(@PathVariable String id) {
        List<AnswerSummary> list = collectionService.getAnswerList(id);
        return new ResponseEntity<>(
                new ResponseBody(Status.SUCCESS, list),
                HttpStatus.OK
        );
    }

    @GetMapping("/table/{tableId}/answer/{AnswerId}")
    public ResponseEntity<ResponseBody> getAnswers(
            @PathVariable String tableId,
            @PathVariable String AnswerId
    ) {
        try{
            TableAnswer tableAnswer = collectionService.getOneTableUserAnswer(tableId, AnswerId);
            return new ResponseEntity<>(
                    new ResponseBody(Status.SUCCESS, tableAnswer),
                    HttpStatus.OK
            );
        }
        catch (TableNotExistException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.TABLE_NOT_EXIST),
                    HttpStatus.NOT_FOUND
            );
        }
        catch (AnswerNotExistException e) {
            return new ResponseEntity<>(
                    new ResponseBody(Status.DATA_NOT_EXIST),
                    HttpStatus.NOT_FOUND
            );
        }
        catch (Exception e) {
            ResponseBody body = new ResponseBody(Status.ACCESS_UNKNOWN_ERROR);
            body.setMessage(e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
        }
    }
}
