package com.practicum.neuron.service;

import com.practicum.neuron.entity.FillRule;
import com.practicum.neuron.entity.question.Question;
import com.practicum.neuron.entity.table.AdminTableSummary;
import com.practicum.neuron.entity.table.Table;
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
     * @throws TableNotExistException         采集表不存在
     * @throws TableAlreadyPublishedException 采集表已发布
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
     * @throws TableNotExistException         采集表不存在
     * @throws TableAlreadyPublishedException 采集表已发布
     * @throws TableAlreadyEndException       采集任务已结束
     * @see FillRule
     */
    void releaseTable(String id, LocalDateTime beginning, LocalDateTime deadline, FillRule rule)
            throws TableNotExistException, TableAlreadyPublishedException, TableAlreadyEndException;

    /**
     * 暂停发布
     *
     * @param id 采集表 tableId
     * @throws TableNotExistException   采集表不存在
     * @throws TableUnpublishException  采集表未发布
     * @throws TableAlreadyEndException 采集任务已结束
     */
    void stopRelease(String id) throws TableNotExistException, TableUnpublishException, TableAlreadyEndException, TableNotExistException;

    /**
     * 删除表
     *
     * @param id 采集表 tableId
     * @throws TableNotExistException 采集表不存在
     */
    void deleteTable(String id) throws TableNotExistException;


    /**
     * 根据用户名查找此用户所有保存的采集表，只返回每个表的简要信息
     *
     * @param username 用户名
     * @return 采集表简要信息
     */
    List<AdminTableSummary> getTableSummary(String username);

    /**
     * 根据 id 查找指定采集表
     *
     * @param id 表 id
     * @return 采集表
     * @throws TableNotExistException   采集表不存在
     */
    Table getTable(String id) throws TableNotExistException;
}
