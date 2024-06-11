package com.practicum.neuron.serviceImpl;

import com.practicum.neuron.entity.QuestionAnswer;
import com.practicum.neuron.entity.ReleaseInfo;
import com.practicum.neuron.entity.SubmitInfo;
import com.practicum.neuron.entity.answer.Answer;
import com.practicum.neuron.entity.table.Table;
import com.practicum.neuron.entity.table.UserTableAnswer;
import com.practicum.neuron.entity.table.UserTableSummary;
import com.practicum.neuron.exception.TableNotExistException;
import com.practicum.neuron.mapper.*;
import com.practicum.neuron.service.FillService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FillServiceImpl implements FillService {
    @Resource
    private TableMapper tableMapper;

    @Resource
    private ReleaseInfoMapper releaseInfoMapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private AnswerMapper answerMapper;

    @Resource
    private SubmitInfoMapper submitInfoMapper;

    @Override
    public List<UserTableSummary> getTableSummary(String username) {

        List<ReleaseInfo> list =  releaseInfoMapper.findAllByBeginningBeforeAndDeadlineAfter(
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        List<UserTableSummary> summaryList = new ArrayList<>();
        for (ReleaseInfo releaseInfo : list) {
            // 只有满足填写规则的才会返回
            if (releaseInfo.getFillRule().canFill(accountMapper.findUserInfoByUsername(username))) {
                summaryList.add(
                        UserTableSummary.builder()
                                .id(releaseInfo.getTableId())
                                .title(releaseInfo.getTitle())
                                .author(releaseInfo.getAuthor())
                                .beginning(releaseInfo.getBeginning())
                                .deadline(releaseInfo.getDeadline())
                                .build()
                );
            }
        }
        return summaryList;
    }

    @Override
    public Table getTable(String id) throws TableNotExistException {
        Optional<Table> t =  tableMapper.findById(id);
        if(t.isPresent()) {
            return t.get();
        }
        else {
            throw new TableNotExistException();
        }
    }

    @Override
    public void saveAnswer(String id, String respondent, List<Answer> answers) throws TableNotExistException {
        if(tableMapper.findById(id).isPresent()) {
            // 先删除原来提交的答案
            answerMapper.deleteAllByTableIdAndRespondent(id, respondent);

            // 再将答案全部插入至数据库
            answerMapper.insert(answers);
        }
        else {
            throw new TableNotExistException();
        }
    }

    @Override
    public void submitAnswer(String id, String respondent, LocalDateTime date) {
        Optional<SubmitInfo> s = submitInfoMapper.findByTableIdAndRespondent(id, respondent);
        // 如果表已经存在，就修改数据
        if(s.isPresent()) {
            SubmitInfo submitInfo = s.get();
            submitInfo.setTableId(id);
            submitInfo.setRespondent(respondent);
            submitInfo.setDate(date);
            submitInfoMapper.save(submitInfo);
        }
        // 否则插入新数据
        else {
            submitInfoMapper.insert(
                    SubmitInfo.builder()
                            .tableId(id)
                            .respondent(respondent)
                            .date(date)
                            .build()
            );
        }
    }

    @SneakyThrows
    @Override
    public UserTableAnswer getTableAnswer(String id, String respondent) {
        Optional<Table> t = tableMapper.findById(id);
        if (t.isPresent()) {
            Table table = t.get();
            List<Document> questions = table.getQuestions();
            List<QuestionAnswer> questionAnswer = new ArrayList<>();
            for (Document question : questions) {
                String fingerprint = question.getString("fingerprint");
                Optional<Answer> answers = answerMapper.findByTableIdAndRespondentAndFingerprint(
                        id,
                        respondent,
                        fingerprint
                );
                if (answers.isPresent()) {
                    questionAnswer.add(new QuestionAnswer(question, answers.get().getAnswers()));
                }
                else {
                    questionAnswer.add(new QuestionAnswer(question, new String[]{}));
                }
            }
            return UserTableAnswer.builder()
                    .title(table.getTitle())
                    .questionAnswers(questionAnswer)
                    .build();
        }
        else {
            throw new TableNotExistException();
        }
    }
}
