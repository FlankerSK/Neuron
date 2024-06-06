package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.table.Table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableMapper extends MongoRepository<Table, String> {
    @Override
    @NonNull
    Optional<Table> findById(@NonNull String s);

    @Override
    @NonNull
    <S extends Table> S insert(@NonNull S entity);

    @Override
    @NonNull
    <S extends Table> S save(@NonNull S entity);

    @Override
    void deleteById(@NonNull String s);

    @Override
    void deleteAll();
}
