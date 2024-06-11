package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.answer.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface AnswerMapper extends MongoRepository<Answer, String> {
    /**
     * 指定表 id 和 回答者，删除所有的表
     *
     * @param tableId    the table id
     * @param respondent the respondent
     */
    void deleteAllByTableIdAndRespondent(String tableId, String respondent);

    Optional<Answer> findByTableIdAndRespondentAndFingerprint(
            String tableId,
            String respondent,
            String fingerprint
    );
}