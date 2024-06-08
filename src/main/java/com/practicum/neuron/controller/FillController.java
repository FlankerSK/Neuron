package com.practicum.neuron.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
}
