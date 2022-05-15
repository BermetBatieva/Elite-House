package com.example.HRMS.services;

import com.example.HRMS.DTO.AnswerDTO;
import com.example.HRMS.DTO.QuestionList;
import com.example.HRMS.entity.Answer;
import com.example.HRMS.entity.Question;
import com.example.HRMS.enums.Status;
import com.example.HRMS.repository.AnswerRepository;
import com.example.HRMS.repository.QuestionRepository;
import com.example.HRMS.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public List<QuestionList> findAllByTest(Long testId) {
        List<Question> questions = questionRepository.findAllByTest_Id(testId);

        List<QuestionList> questionLists = new ArrayList<>();

        for (Question q : questions) {
            QuestionList question = new QuestionList();
            question.setQuestion(q.getQuestion());
            question.setQuestionId(q.getId());
            question.setType(q.getTypeQuestion());

            List<Answer> answerList = answerRepository.findAllByQuestion_IdAndStatus(q.getId(), Status.ACTIVE);
            List<AnswerDTO> answerDTOS = new ArrayList<>();
            for (Answer a : answerList) {
                AnswerDTO answerDTO = new AnswerDTO();
                answerDTOS.add(answerDTO);
            }
            question.setAnswers(answerDTOS);
            questionLists.add(question);
        }
        return questionLists;
    }
}
