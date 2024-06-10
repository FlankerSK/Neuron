package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.SubmitInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubmitInfoMapper extends MongoRepository<SubmitInfo, String> {
}
