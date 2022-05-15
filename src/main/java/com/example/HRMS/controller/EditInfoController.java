package com.example.HRMS.controller;

import com.example.HRMS.DTO.*;
import com.example.HRMS.services.DocumentService;
import com.example.HRMS.services.UserDetailsServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/edit")
public class EditInfoController {

    @Autowired
    private UserDetailsServiceImpl userServiceImpl;

    @Autowired
    private DocumentService documentService;

    @ApiOperation(value = "Смена пароля")
    @PutMapping("/password")
    public ResponseEntity<MessageResponse> setNewPassword(@RequestBody ChangePasswordDto dto){
        return new ResponseEntity<>(userServiceImpl.changePassword(dto), HttpStatus.OK);
    }

    @ApiOperation(value = "Запрос на изменение почты (для авторизованного пользователя)")
    @PostMapping("/new-email-confirmation-request")
    public ResponseEntity<ResponseMessage> request(@RequestBody EmailDto emailDto) {
        return new ResponseEntity<>(userServiceImpl.changeEmailRequest(emailDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Подтверждение новой почты и его смена (юзеру нужно чз дто дать пароль от системы HRMS)")
    @PostMapping("/set-new-email/key/{key}")
    public ResponseEntity<AuthenticationResponse> checkIt(@PathVariable String key, @RequestBody PasswordDto dto){
        return userServiceImpl.checkNewEmailConfirmationAndAuth(key, dto);
    }

    @ApiOperation(value = "Получение личных данных текущего пользователя")
    @GetMapping("/get-user-data")
    public ResponseEntity<InfoMessage> getUserData(){
        return userServiceImpl.getUserInfo();
    }

    @ApiOperation(value = "Изменение личных данных (Анкета)")
    @PutMapping("/personal-data")
    public ResponseMessage registration(@RequestBody CandidateRegistrationDto candidateRegistrationDto) {
        return userServiceImpl.updatePersonalDataNoPosition(candidateRegistrationDto);
    }

    @ApiOperation(value = "Обновление резюме")
    @PutMapping("/update/resume")
    public ResponseMessage updateResume(
            @RequestParam(name = "resume") MultipartFile resume) {
        return documentService.updateResume(resume);
    }

    @ApiOperation(value = "Удаление документа по его айди")
    @DeleteMapping("/delete/document")
    public ResponseMessage deleteDocument(@RequestBody IdDto dto){
        return documentService.deleteDocumentById(dto.getId());
    }

    @ApiOperation(value = "Добавление рекоммендационных писем")
    @PostMapping("/add/recommendations")
    public ResponseMessage addRecommendations(@RequestParam(name = "recommendations") MultipartFile[] recommendations){
        return documentService.addRecommendations(recommendations);
    }

    @ApiOperation(value = "Добавление доп файлов")
    @PostMapping("/add/additional-files")
    public ResponseMessage addAdditionalFiles(@RequestParam(name = "additionalFiles") MultipartFile[] additionalFiles){
        return documentService.addAdditionalFiles(additionalFiles);
    }

    //TODO: write a method that adds new profile photo
    @ApiOperation(value = "Добавление фото профиля")
    @PostMapping("/add/profile-photo")
    public ResponseMessage addProfilePhoto(@RequestParam(name = "photo") MultipartFile photo){
        return documentService.addProfilePhoto(photo);
    }

    @ApiOperation(value = "Удаление фото профиля по айди")
    @DeleteMapping("/delete/profile-photo")
    public ResponseMessage deleteProfilePhoto(@RequestBody IdDto dto){
        return documentService.deletePhotoById(dto.getId());
    }

    @ApiOperation(value = "Обновление фото профиля")
    @PutMapping("/update/profile-photo")
    public ResponseMessage updatePhoto(@RequestParam(name = "photo") MultipartFile photo){
        return documentService.updatePhoto(photo);
    }
}
