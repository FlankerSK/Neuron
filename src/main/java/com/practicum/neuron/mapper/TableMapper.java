package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.table.Table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableMapper extends MongoRepository<Table, String> {
    List<Table> findTablesByAuthor(String author);
}
