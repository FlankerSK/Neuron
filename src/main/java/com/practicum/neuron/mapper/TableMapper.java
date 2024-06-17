package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.table.Table;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TableMapper extends MongoRepository<Table, String> {
    List<Table> findTablesByAuthor(String author);
}
