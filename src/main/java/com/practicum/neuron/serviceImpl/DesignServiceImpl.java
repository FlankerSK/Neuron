package com.practicum.neuron.serviceImpl;

import com.practicum.neuron.entity.FillRule;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.exception.TableNotExistException;
import com.practicum.neuron.mapper.TableMapper;
import com.practicum.neuron.service.DesignService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DesignServiceImpl implements DesignService {
    @Resource
    private TableMapper tableMapper;

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
    public void updateQuestion(String id, List<Document> questions) throws TableNotExistException {
        Optional<Table> t = tableMapper.findById(id);
        if(t.isPresent()) {
            Table table = t.get();
            table.setUpdateDate(new Date());
            table.setQuestions(questions);
            tableMapper.save(table);
        }
        else {
            throw new TableNotExistException();
        }
    }

    @Override
    public void releaseTable(int id, Date deadline, FillRule rule) {

    }

    @Override
    public void deleteTable(int id) {

    }
}
