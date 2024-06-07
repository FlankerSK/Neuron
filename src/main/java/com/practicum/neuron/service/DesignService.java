package com.practicum.neuron.service;

import com.practicum.neuron.entity.FillRule;
import com.practicum.neuron.entity.question.Question;
import com.practicum.neuron.exception.TableAlreadyEndException;
import com.practicum.neuron.exception.TableAlreadyPublishedException;
import com.practicum.neuron.exception.TableNotExistException;
import com.practicum.neuron.exception.TableUnpublishException;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
     * @return 采集表 tableId
     */
    String createTable(String tittle, String author);

    /**
     * 更新指定采集表的问题列表
     *
     * @param id        采集表 id
     * @param title     采集表标题
     * @param questions 问题列表, 实际的类型是 Question 及其子类
     * @see Question
     */
    void updateQuestion(String id, String title, List<Document> questions)
            throws TableNotExistException, TableAlreadyPublishedException;

    /**
     * 发布表
     *
     * @param id        采集表 tableId
     * @param beginning 开始日期
     * @param deadline  截止日期
     * @param rule      填写规则约束
     * @see FillRule
     */
    void releaseTable(String id, LocalDateTime beginning, LocalDateTime deadline, FillRule rule)
            throws TableNotExistException, TableAlreadyPublishedException, TableAlreadyEndException;

    /**
     * 暂停发布
     *
     * @param id 采集表 tableId
     */
    void stopRelease(String id) throws TableUnpublishException, TableAlreadyEndException, TableNotExistException;

    /**
     * 删除表
     *
     * @param id 采集表 tableId
     */
    void deleteTable(String id) throws TableNotExistException;

    //List<TableSummary> getTableSummary();


    //Table getTable(int tableId);
}
