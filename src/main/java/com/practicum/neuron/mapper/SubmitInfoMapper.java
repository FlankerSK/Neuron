package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.SubmitInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubmitInfoMapper extends MongoRepository<SubmitInfo, String> {


    /**
     * 根据表 id 查找所有提交信息
     *
     * @param tableId 表 id
     * @return 提交信息列表
     */
    List<SubmitInfo> findAllByTableId(String tableId);

    /**
     * 根据表 id 和回答者用户名查找提交信息
     *
     * @param tableId 表 id
     * @param respondent 回答者
     * @return Optional包装的提交信息对象
     */
    Optional<SubmitInfo> findByTableIdAndRespondent(String tableId, String respondent);
}
