package com.practicum.neuron.service;

import com.practicum.neuron.entity.FillRule;
import com.practicum.neuron.entity.question.Question;
import com.practicum.neuron.exception.TableNotExistException;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 采集表相关服务接口
 */
@Service
public interface DesignService {

    /**
     * 创建采集表
     *
     * @param tittle 表标题
     * @param author 创建者
     * @return 采集表 id
     */
    String createTable(String tittle, String author);

    /**
     * 更新指定采集表的问题列表
     *
     * @param id        采集表 id
     * @param questions 问题列表, 实际的类型是 Question 及其子类
     * @see Question
     */
    void updateQuestion(String id, List<Document> questions) throws TableNotExistException;


    /**
     * 发布表
     *
     * @param id       采集表 id
     * @param deadline 截止日期
     * @param rule     填写规则约束
     * @see FillRule
     */
    void releaseTable(int id, Date deadline, FillRule rule);


    /**
     * 删除表
     *
     * @param id 采集表 id
     */
    void deleteTable(int id);


    //List<TableSummary> getTableSummary();


    //Table getTable(int id);
}
