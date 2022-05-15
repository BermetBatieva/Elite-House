package com.example.HRMS.controller;

import com.example.HRMS.DTO.OpenAnswerCheck;
import com.example.HRMS.DTO.ResponseMessage;
import com.example.HRMS.DTO.TestQuestion;
import com.example.HRMS.DTO.TestsForUsers;
import com.example.HRMS.services.TestService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @ApiOperation(value = "Получение всех доступных тестов пользователя")
    @GetMapping("/get-with-current-user")
    private List<TestsForUsers> findByUser() {
        return testService.findByTestsCurrentUser();
    }

    @ApiOperation(value = "Кнопка 'начать тест'. Start теста")
    @PostMapping("/start")
    private ResponseMessage startTest(@RequestBody TestsForUsers test) {
        return testService.startTest(test);
    }

    @ApiOperation(value = "Получение всех вопросов теста по testId")
    @GetMapping("/get-all-questions/{testId}")
    private List<TestQuestion> findAllTestQuestion(@PathVariable Long testId) {
        return testService.findAllTestQuestion(testId);
    }

    @ApiOperation(value = "Отправка результатов. То что выбрал пользователь.")
    @PostMapping(value = "/set-test-results", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseMessage setTestResults(@RequestBody List<TestQuestion> testResult) {
        return testService.setTestResults(testResult);
    }

    @ApiOperation(value = "Получение всех openAnswer пользователя чтобы проверить")
    @GetMapping("/get-all-open-answer/{resultId}")
    private List<OpenAnswerCheck> findAllOpenAnswer(@PathVariable Long resultId) {
        return testService.findAllOpenAnswerResult(resultId);
    }

    @ApiOperation(value = "Отправка проверенных openAnswer вопросов")
    @GetMapping("/set-result-checked-open-answer/{testId}")
    private ResponseMessage setResultCheckedOpenAnswer(@RequestBody List<OpenAnswerCheck> openAnswerCheckList,
                                                       @PathVariable Long testId) {
        return testService.setResultCheckedOpenAnswer(openAnswerCheckList, testId);
    }

}
