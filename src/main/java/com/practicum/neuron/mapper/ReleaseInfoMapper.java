package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.ReleaseInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReleaseInfoMapper extends MongoRepository<ReleaseInfo, String> {
    void deleteByTableId(String id);

    List<ReleaseInfo> findByBeginningBeforeAndDeadlineAfter(LocalDateTime beginning, LocalDateTime deadline);
}
