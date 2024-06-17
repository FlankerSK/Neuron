package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.ReleaseInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReleaseInfoMapper extends MongoRepository<ReleaseInfo, String> {
    void deleteByTableId(String id);

    List<ReleaseInfo> findAllByBeginningBeforeAndDeadlineAfter(LocalDateTime date1, LocalDateTime date2);

    List<ReleaseInfo> findAllByDeadlineBefore(LocalDateTime date);
}
