package com.practicum.neuron.service;

import com.practicum.neuron.entity.answer.Answer;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.entity.table.UserTableAnswer;
import com.practicum.neuron.entity.table.UserTableSummary;
import com.practicum.neuron.exception.TableNotExistException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 *  采集表填写服务接口
 */
@Service
public interface FillService {
    /**
     * 根据用户名查找此用户所有可以填写的采集表，只返回每个表的简要信息
     *
     * @param username 用户名
     * @return 采集表简要信息 table summary
     */
    List<UserTableSummary> getTableSummary(String username);

    /**
     * 根据 id 查找指定采集表
     *
     * @param id 表 id
     * @return 采集表 table
     * @throws TableNotExistException 采集表不存在
     */
    Table getTable(String id) throws TableNotExistException;

    /**
     * 保存指定 id 的采集表的用户填写数据
     *
     * @param id         表 id
     * @param respondent 回答者
     * @param answers    答案列表
     * @throws TableNotExistException 采集表不存在
     */
    void saveAnswer(String id, String respondent, List<Answer> answers) throws TableNotExistException;

    /**
     * 提交答案
     *
     * @param id         表 id
     * @param respondent 回答者
     * @param date       日期
     */
    void submitAnswer(String id, String respondent, LocalDateTime date);


    /**
     * 根据表 id 和回答者获取填写数据
     *
     * @param id         表 id
     * @param respondent 回答者
     * @return 表与对应的填报数据
     * @see UserTableAnswer
     */
    @SneakyThrows
    UserTableAnswer getTableAnswer(String id, String respondent);
}
