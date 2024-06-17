package com.practicum.neuron.service;

import com.practicum.neuron.entity.answer.AnswerSummary;
import com.practicum.neuron.entity.table.AdminTableAnswer;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CollectionService {
    /**
     * 根据表 id 查找所有已提交的填报数据
     *
     * @param tableId 表 id
     * @return 回答摘要列表
     */
    List<AnswerSummary> getAnswerList(String tableId);

    /**
     * 根据表 id 和回答 id 查找提交的填报数据
     *
     * @param tableId  表 id
     * @param answerId 回答 id
     * @return 表与对应的填报数据
     * @see AdminTableAnswer
     */
    @SneakyThrows
    AdminTableAnswer getOneTableUserAnswer(String tableId, String answerId);
}
