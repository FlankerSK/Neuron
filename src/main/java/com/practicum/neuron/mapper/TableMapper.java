package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.table.Table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableMapper extends MongoRepository<Table, String> {
}
