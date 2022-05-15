package com.example.HRMS.controller;

import com.example.HRMS.DTO.EmailDto;
import com.example.HRMS.DTO.MessageResponse;
import com.example.HRMS.DTO.PasswordDto;
import com.example.HRMS.DTO.ResetRequestResponse;
import com.example.HRMS.services.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/reset")
public class ResetPasswordController {

    @Autowired
    private ResetPasswordService resetPasswordService;

    @PostMapping("/request")
    public ResponseEntity<ResetRequestResponse> request(@RequestBody EmailDto emailDto) {
        return new ResponseEntity<>(resetPasswordService.resetRequest(emailDto.getEmail()), HttpStatus.OK);
    }

    @PostMapping("/set-new-password/key/{key}")
    public MessageResponse setNewPassword(@PathVariable String key, @RequestBody PasswordDto dto){
        return resetPasswordService.setNewPassword(key, dto);
    }

}
