package com.practicum.neuron.serviceImpl;

import com.practicum.neuron.entity.FillRule;
import com.practicum.neuron.entity.ReleaseInfo;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.entity.table.TableStatus;
import com.practicum.neuron.exception.TableAlreadyEndException;
import com.practicum.neuron.exception.TableAlreadyPublishedException;
import com.practicum.neuron.exception.TableNotExistException;
import com.practicum.neuron.exception.TableUnpublishException;
import com.practicum.neuron.mapper.ReleaseInfoMapper;
import com.practicum.neuron.mapper.TableMapper;
import com.practicum.neuron.service.DesignService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DesignServiceImpl implements DesignService {
    @Resource
    private TableMapper tableMapper;

    @Resource
    private ReleaseInfoMapper releaseInfoMapper;

    @Override
    public String createTable(String tittle, String author) {
        Table table = Table.builder()
                .tittle(tittle)
                .author(author)
                .build();
        Table ret = tableMapper.insert(table);
        return ret.getId();
    }

    @Override
    public void updateQuestion(String id, String title, List<Document> questions)
            throws TableNotExistException, TableAlreadyPublishedException {
        Optional<Table> t = tableMapper.findById(id);
        if(t.isPresent()) {
            Table table = t.get();
            if (table.getStatus().equals(TableStatus.PUBLISHING)) {
                throw new TableAlreadyPublishedException();
            }
            table.setTittle(title);
            table.setUpdateDate(LocalDateTime.now());
            table.setQuestions(questions);
            table.setStatus(TableStatus.UNPUBLISH);
            tableMapper.save(table);
        }
        else {
            throw new TableNotExistException();
        }
    }

    @Override
    public void releaseTable(String id, LocalDateTime beginning, LocalDateTime deadline, FillRule rule)
            throws TableNotExistException, TableAlreadyPublishedException, TableAlreadyEndException {
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
                    .beginning(beginning)
                    .deadline(deadline)
                    .build();
            releaseInfoMapper.insert(info);
        }
        else {
            throw new TableNotExistException();
        }
    }

    @Override
    public void stopRelease(String id) throws TableUnpublishException, TableAlreadyEndException, TableNotExistException {
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

    }
}
