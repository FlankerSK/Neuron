package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.answer.LastAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface LastAnswerMapper extends MongoRepository<LastAnswer, String> {

    /**
     * 根据问题的数字指纹和回答者查找上次填写的数据
     *
     * @param fingerprint 问题的数字指纹
     * @param respondent  回答者
     * @return Optional包装的上次填写的数据
     */
    Optional<LastAnswer> findByFingerprintAndRespondent(String fingerprint, String respondent);
}
