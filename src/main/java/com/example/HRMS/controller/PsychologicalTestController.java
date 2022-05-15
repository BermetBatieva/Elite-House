package com.example.HRMS.controller;

import com.example.HRMS.DTO.*;
import com.example.HRMS.entity.PsychologicalQuestions;
import com.example.HRMS.entity.PsychologicalType;
import com.example.HRMS.services.PsychologicalTestService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PostRemove;
import java.util.List;

@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/psychological")
public class PsychologicalTestController {

    @Autowired
    private PsychologicalTestService psychologicalTestService;


    @ApiOperation("создание психологического типа")
    @PostMapping("type-create")
    public PsychologicalType createPsychType(@RequestBody PsychologicalTypeDto dto){
        return psychologicalTestService.createPsychologicalType(dto);
    }

    @ApiOperation("создание психологического вопроса")
    @PostMapping("question-create")
    public PsychologicalQuestions createPsychQuestion(@RequestBody PsychologicalQuestionDto dto){
        return psychologicalTestService.createPsychologicalQuestion(dto);
    }


    @ApiOperation("отправка списка с числами(оценками на вопрос)")
    @PostMapping(value = "/set-temp-of-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage setTempScoreOfUser(@RequestBody List<PsychologicalTestQuestion> psychAnswerDtos){
        return psychologicalTestService.setTempOfUser(psychAnswerDtos);
    }

    @ApiOperation("все психологические вопросы")
    @GetMapping("/questions/all-active")
    public List<PsychologicalTestQuestion> getAllActivePsychQuestions(){
        return psychologicalTestService.findAllPsychQuestion();
    }

    @ApiOperation("все психологические типы")
    @GetMapping("/types/all-active")
    public List<PsychologicalType> getAllPsychTypes(){
        return psychologicalTestService.allTypes();
    }

    @ApiOperation(value = "Добавление картинок к вопросам псих.теста")
    @PostMapping("/add-image/{questionId}")
    public void addImageToQuestion(@PathVariable Long questionId, @RequestParam("image") MultipartFile image) {
        psychologicalTestService.addImageToQuestion(questionId, image);
    }
}
