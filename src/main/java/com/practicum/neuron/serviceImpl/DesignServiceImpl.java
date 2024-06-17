package com.practicum.neuron.serviceImpl;

import com.practicum.neuron.entity.FillRule;
import com.practicum.neuron.entity.ReleaseInfo;
import com.practicum.neuron.entity.table.AdminTableSummary;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.entity.table.TableStatus;
import com.practicum.neuron.exception.TableAlreadyEndException;
import com.practicum.neuron.exception.TableAlreadyPublishedException;
import com.practicum.neuron.exception.TableNotExistException;
import com.practicum.neuron.exception.TableUnpublishException;
import com.practicum.neuron.mapper.AnswerMapper;
import com.practicum.neuron.mapper.ReleaseInfoMapper;
import com.practicum.neuron.mapper.SubmitInfoMapper;
import com.practicum.neuron.mapper.TableMapper;
import com.practicum.neuron.service.DesignService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * The type Design service.
 */
@Slf4j
@Component
public class DesignServiceImpl implements DesignService {
    @Resource
    private TableMapper tableMapper;

    @Resource
    private ReleaseInfoMapper releaseInfoMapper;
    @Autowired
    private SubmitInfoMapper submitInfoMapper;
    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public String createTable(String tittle, String author) {
        Table table = Table.builder()
                .title(tittle)
                .author(author)
                .build();
        Table ret = tableMapper.insert(table);
        return ret.getId();
    }

    @Override
    public void updateQuestion(String id, String title, List<Document> questions)
            throws TableNotExistException, TableAlreadyPublishedException, NoSuchAlgorithmException {
        Optional<Table> t = tableMapper.findById(id);
        if(t.isPresent()) {
            Table table = t.get();
            if (table.getStatus().equals(TableStatus.PUBLISHING)) {
                throw new TableAlreadyPublishedException();
            }
            table.setTitle(title);
            table.setUpdateDate(LocalDateTime.now());

            //  给每个问题都提供一个数字指纹
            for (Document question : questions) {
                question.remove("fingerprint");
                log.info(question.toJson());
                byte[] data = question.toJson().getBytes(StandardCharsets.UTF_8);
                byte[] fingerprint = MessageDigest.getInstance("SHA-256").digest(data);
                log.info(Base64.getEncoder().encodeToString(fingerprint));
                question.put("fingerprint", Base64.getEncoder().encodeToString(fingerprint));
            }
            table.setQuestions(questions);
            table.setStatus(TableStatus.UNPUBLISH);
            tableMapper.save(table);
        }
        else {
            throw new TableNotExistException();
        }
    }

    @Override
    public void releaseTable(
            String id,
            LocalDateTime beginning,
            LocalDateTime deadline,
            FillRule rule
    ) throws TableNotExistException, TableAlreadyPublishedException, TableAlreadyEndException {
        Optional<Table> t = tableMapper.findById(id);
        if(t.isPresent()) {
            Table table = t.get();
            switch (table.getStatus()) {
                case UNPUBLISH -> table.setStatus(TableStatus.PUBLISHING);
                case PUBLISHING -> throw new TableAlreadyPublishedException();
                case END -> throw new TableAlreadyEndException();
            }
            tableMapper.save(table);
            ReleaseInfo info = ReleaseInfo.builder()
                    .tableId(id)
                    .title(table.getTitle())
                    .author(table.getAuthor())
                    .beginning(beginning)
                    .deadline(deadline)
                    .fillRule(rule)
                    .build();
            releaseInfoMapper.insert(info);
        }
        else {
            throw new TableNotExistException();
        }
    }

    @Override
    public void stopRelease(String id) throws TableNotExistException, TableUnpublishException, TableAlreadyEndException {
        Optional<Table> t = tableMapper.findById(id);
        if(t.isPresent()) {
            Table table = t.get();
            switch (table.getStatus()) {
                case UNPUBLISH -> throw new TableUnpublishException();
                case PUBLISHING -> table.setStatus(TableStatus.UNPUBLISH);
                case END -> throw new TableAlreadyEndException();
            }
            releaseInfoMapper.deleteByTableId(id);
            tableMapper.save(table);
        }
        else {
            throw new TableNotExistException();
        }
    }

    @Override
    public void deleteTable(String id) throws TableNotExistException {
        // 删除所有库中有对应 id 的数据
        tableMapper.deleteById(id);
        submitInfoMapper.deleteById(id);
        answerMapper.deleteAllByTableId(id);
        releaseInfoMapper.deleteByTableId(id);
    }

    @Override
    public List<AdminTableSummary> getTableSummary(String username) {
        List<Table> tableList = tableMapper.findTablesByAuthor(username);
        List<AdminTableSummary> tableSummaryList = new ArrayList<>();
        for (Table table : tableList) {
            tableSummaryList.add(
                    AdminTableSummary.builder()
                            .id(table.getId())
                            .title(table.getTitle())
                            .updateDate(table.getUpdateDate())
                            .status(table.getStatus())
                            .build()
            );
        }
        return tableSummaryList;
    }

    @Override
    public Table getTable(String id) throws TableNotExistException{
        Optional<Table> t =  tableMapper.findById(id);
        if(t.isPresent()) {
            return t.get();
        }
        else {
            throw new TableNotExistException();
        }
    }


    /**
     * 定时任务，检查提交的采集表是否已截止
     */
    @Scheduled(fixedRate = 60000)   // 60秒检查一次
    public void checkReleaseTableEnd() {
        LocalDateTime now = LocalDateTime.now();
        List<ReleaseInfo> releaseInfoList = releaseInfoMapper.findAllByDeadlineBefore(now);
        for (ReleaseInfo releaseInfo : releaseInfoList) {
            String tableId = releaseInfo.getTableId();
            Optional<Table> t = tableMapper.findById(tableId);
            if(t.isPresent()) {
                Table table = t.get();
                table.setStatus(TableStatus.END);
                tableMapper.save(table);
            }
        }
        releaseInfoMapper.deleteAll(releaseInfoList);
    }
}
