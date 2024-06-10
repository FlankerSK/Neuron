package com.practicum.neuron.service;

import com.practicum.neuron.entity.Answer;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.entity.table.UserTableSummary;
import com.practicum.neuron.exception.TableNotExistException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FillService {
    /**
     * 根据用户名查找此用户所有可以填写的采集表，只返回每个表的简要信息
     *
     * @param username 用户名
     * @return 采集表简要信息
     */
    List<UserTableSummary> getTableSummary(String username);

    /**
     * 根据 id 查找指定采集表
     *
     * @param id 表 id
     * @return 采集表
     * @throws TableNotExistException   采集表不存在
     */
    Table getTable(String id) throws TableNotExistException;

    /**
     * 保存指定 id 的采集表的用户填写数据
     *
     * @param id 表 id
     * @param respondent 回答者
     * @param answers 答案列表
     * @throws TableNotExistException   采集表不存在
     */
    void saveAnswer(String id, String respondent, List<Answer> answers) throws TableNotExistException;
}
