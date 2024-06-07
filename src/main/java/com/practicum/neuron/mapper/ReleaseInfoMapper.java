package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.ReleaseInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReleaseInfoMapper extends MongoRepository<ReleaseInfo, String> {
    void deleteByTableId(String id);
}
