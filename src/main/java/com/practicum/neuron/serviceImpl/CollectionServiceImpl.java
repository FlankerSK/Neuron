package com.practicum.neuron.serviceImpl;

import com.practicum.neuron.entity.QuestionAnswer;
import com.practicum.neuron.entity.SubmitInfo;
import com.practicum.neuron.entity.answer.Answer;
import com.practicum.neuron.entity.answer.AnswerSummary;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.entity.table.TableAnswer;
import com.practicum.neuron.exception.AnswerNotExistException;
import com.practicum.neuron.exception.TableNotExistException;
import com.practicum.neuron.mapper.AnswerMapper;
import com.practicum.neuron.mapper.SubmitInfoMapper;
import com.practicum.neuron.mapper.TableMapper;
import com.practicum.neuron.service.CollectionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CollectionServiceImpl implements CollectionService {
    @Resource
    private SubmitInfoMapper submitInfoMapper;

    @Resource
    private AnswerMapper answerMapper;

    @Resource
    private TableMapper tableMapper;

    @Override
    public List<AnswerSummary> getAnswerList(String tableId) {
        List<SubmitInfo> list = submitInfoMapper.findAllByTableId(tableId);
        List<AnswerSummary> answerSummaryList = new ArrayList<>();
        for (SubmitInfo submitInfo : list){
            answerSummaryList.add(
                    AnswerSummary.builder()
                            .answerId(submitInfo.getId())
                            .tableId(tableId)
                            .respondent((submitInfo.getId()))
                            .date(submitInfo.getDate())
                            .build()
            );
        }
        return answerSummaryList;
    }

    @Override
    public TableAnswer getOneTableUserAnswer(String tableId, String answerId) throws AnswerNotExistException, TableNotExistException {
        Optional<SubmitInfo> s = submitInfoMapper.findById(answerId);
        if (s.isPresent()) {
            SubmitInfo submitInfo = s.get();
            String respondent = submitInfo.getRespondent();
            Optional<Table> t = tableMapper.findById(tableId);
            if (t.isPresent()) {
                Table table = t.get();
                List<Document> questions = table.getQuestions();
                List<QuestionAnswer> questionAnswer = new ArrayList<>();
                for(Document question : questions) {
                    String fingerprint = question.getString("fingerprint");
                    Optional<Answer> answers = answerMapper.findByTableIdAndRespondentAndFingerprint(
                            tableId,
                            respondent,
                            fingerprint
                    );
                    if (answers.isPresent()) {
                        questionAnswer.add(new QuestionAnswer(question, answers.get().getAnswers()));
                    }
                    else {
                        throw new AnswerNotExistException();
                    }
                }
                return TableAnswer.builder()
                        .title(table.getTitle())
                        .respondent(respondent)
                        .date(submitInfo.getDate())
                        .questionAnswers(questionAnswer)
                        .build();
            }
            else {
                throw new TableNotExistException();
            }
        }
        else {
            throw new AnswerNotExistException();
        }
    }
}
