package com.example.HRMS.services;

import com.example.HRMS.DTO.*;
import com.example.HRMS.entity.*;
import com.example.HRMS.enums.PsychologicalQuestionType;
import com.example.HRMS.enums.Status;
import com.example.HRMS.enums.StatusTest;
import com.example.HRMS.exception.ResourceNotFoundException;
import com.example.HRMS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PsychologicalTestService {

    @Autowired
    private PsychologicalTypeRepository typeRepository;

    @Autowired
    private PsychologicalQuestionsRepository psychologicalQuestionsRepository;

    @Autowired
    private ImagesService imagesService;

    @Autowired
    private PsychologicalResultsRepository psychologicalResultsRepository;

    @Autowired
    private PsychologicalReviewRepository psychologicalReviewRepository;

    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired
    private PsychologicalTypeRepository psychologicalTypeRepository;

    @Autowired
    private GeneralResultRepository generalResultRepository;

    private static final int bigFive = 5;

    public List<PsychologicalType> allTypes(){
        List<PsychologicalType> psychologicalTypes = psychologicalTypeRepository.findByStatus(Status.ACTIVE);
        return psychologicalTypes;
    }

    public PsychologicalType createPsychologicalType(PsychologicalTypeDto psychologicalTypeDto) {
        if (!typeRepository.existsByName(psychologicalTypeDto.getName())) {
            PsychologicalType psychologicalType = new PsychologicalType();
            psychologicalType.setName(psychologicalTypeDto.getName());
            psychologicalType.setInitPoint(psychologicalTypeDto.getInitPoint());
            psychologicalType.setStatus(Status.ACTIVE);
            typeRepository.save(psychologicalType);
            return psychologicalType;
        } else
            throw new ResourceNotFoundException("not saved");
    }

    public PsychologicalQuestions createPsychologicalQuestion(PsychologicalQuestionDto dto) {
        if (!psychologicalQuestionsRepository.existsByQuestion(dto.getQuestion())) {
            PsychologicalQuestions psychologicalQuestions = new PsychologicalQuestions();
            psychologicalQuestions.setPsychologicalType(typeRepository.findById(dto.getPsychologicalTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Нет типа с таким ID ", dto.getPsychologicalTypeId())));
            psychologicalQuestions.setQuestion(dto.getQuestion());
            psychologicalQuestions.setSign(dto.getQuestionType());
            psychologicalQuestions.setStatus(Status.ACTIVE);
            psychologicalQuestionsRepository.save(psychologicalQuestions);
            return psychologicalQuestions;
        } else
            throw new ResourceNotFoundException("not saved");
    }

    public void addImageToQuestion(Long questionId, MultipartFile photo) {

        PsychologicalQuestions question = psychologicalQuestionsRepository.
                findById(questionId).orElseThrow(
                        () -> new ResourceNotFoundException("Вопрос не найден! questionId = " + questionId));

        question.setPhoto(imagesService.set(photo, question.getQuestion()
                .replaceAll("\\s+", "_") + "_" + question.getId()));
        psychologicalQuestionsRepository.save(question);
    }


    Date convertLocalDateTimeToDateUsingInstant(LocalDateTime dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }


    public List<PsychologicalTestQuestion> findAllPsychQuestion() {
        List<PsychologicalTestQuestion> questionList = new ArrayList<>();

        for (PsychologicalQuestions psychologicalQuestion : psychologicalQuestionsRepository.findAllByStatus(Status.ACTIVE)) {
            PsychologicalTestQuestion questionModel = new PsychologicalTestQuestion();
            questionModel.setQuestion(psychologicalQuestion.getQuestion());
            questionModel.setImage(psychologicalQuestion.getPhoto());
            questionModel.setPsychType(psychologicalQuestion.getPsychologicalType().getName());
            questionModel.setQuestionId(psychologicalQuestion.getId());
            questionList.add(questionModel);
        }
        return questionList;

    }

    @Transactional
    public ResponseMessage setTempOfUser(List<PsychologicalTestQuestion> answerDtos) {
        PsychologicalResults psychologicalResults = psychologicalResultsRepository.findByUser_IdAndStatusTest(userService.getCurrentUser().getId(),
                StatusTest.IP);
        List<PsychologicalReview> list = new ArrayList<>();

        for (PsychologicalTestQuestion answer : answerDtos) {
            PsychologicalReview psychologicalReview = new PsychologicalReview();
            psychologicalReview.setTemp(answer.getTemp());
            psychologicalReview.setPsychologicalQuestions(psychologicalQuestionsRepository.getById(answer.getQuestionId()));
            psychologicalReview.setPsychologicalResults(psychologicalResults);
            list.add(psychologicalReviewRepository.save(psychologicalReview));
        }
        psychologicalResults.setStatusTest(StatusTest.CHECKED);
        psychologicalResultsRepository.save(psychologicalResults);

        generalPsychCalculate(list, psychologicalResults.getId());
        return new ResponseMessage("Results successfully added!", 200);

    }


    private void generalPsychCalculate(List<PsychologicalReview> answerDtos, long resultId) {
        List<PsychologicalType> psychologicalTypes = psychologicalTypeRepository.findByStatus(Status.ACTIVE);

        List<GeneralResult> listGeneral = new ArrayList<>();


        for (PsychologicalType psychologicalType : psychologicalTypes) {
            GeneralResult generalResult = new GeneralResult();
            generalResult.setPsychologicalType(psychologicalType);
            generalResult.setPsychologicalResults(psychologicalResultsRepository.getById(resultId));
            generalResult.setGeneralScore(psychologicalType.getInitPoint());
            listGeneral.add(generalResultRepository.save(generalResult));
        }
        for (PsychologicalReview review : answerDtos) {
            GeneralResult generalResult = generalResultRepository.findByPsychologicalResults_IdAndPsychologicalType_Id(resultId,
                    review.getPsychologicalQuestions().getPsychologicalType().getId());
            if (review.getPsychologicalQuestions().getSign() == PsychologicalQuestionType.PLUS)
                generalResult.setGeneralScore(generalResult.getGeneralScore() + review.getTemp());
            else
                generalResult.setGeneralScore(generalResult.getGeneralScore() - review.getTemp());
            generalResultRepository.save(generalResult);
        }
    }
}










