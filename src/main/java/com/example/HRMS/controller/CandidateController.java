package com.example.HRMS.controller;

import com.example.HRMS.DTO.*;
import com.example.HRMS.services.ApplicationService;
import com.example.HRMS.services.DocumentService;
import com.example.HRMS.services.UserDetailsServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin
@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private UserDetailsServiceImpl userServiceImpl;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ApplicationService applicationService;

    @ApiOperation(value = "Окно регистрация - запрос кода на почту")
    @PostMapping("/reg/{mail}")
    public ResponseMessage mailCheck(@PathVariable String mail) {
        return userServiceImpl.checkMail(mail);
    }

    @ApiOperation(value = "Авторизация - получение JWT token")
    @PostMapping("/auth")
    public AuthenticationResponse auth(@RequestBody AuthDTO auth) {
        return userServiceImpl.token(auth);
    }

    @ApiOperation(value = "Окно регистрации  - ввод личных данных (Анкета)")
    @PutMapping("/personal-data-set")
    public ResponseMessage registration(@RequestBody CandidateRegistrationDto candidateRegistrationDto) {
        return userServiceImpl.updatePersonalData(candidateRegistrationDto);
    }

    @ApiOperation(value = "Загрузка фото")
    @PutMapping("/set-photo")
    public void setPhoto(@RequestParam(name = "photo") MultipartFile photo){

    }

    @ApiOperation(value = "Окно регистрации  - загрузка документов")
    @PutMapping("/set-documents")
    public ResponseMessage setDocuments(
                             @RequestParam(name = "resume") MultipartFile resume,
                             @RequestParam(name = "recommendations") MultipartFile[] recommendation,
                             @RequestParam(name = "additionalFiles") MultipartFile[] additionalFiles) {
        return documentService.create(resume, recommendation, additionalFiles);
    }

    @GetMapping("/my-application")
    public ResponseEntity<ApplicationHolder> myApplication(){
        return applicationService.getApplicationStatus();
    }

}
