package com.example.HRMS.services;

import com.example.HRMS.DTO.MessageResponse;
import com.example.HRMS.DTO.PasswordDto;
import com.example.HRMS.DTO.ResetRequestResponse;
import com.example.HRMS.entity.ResetPassword;
import com.example.HRMS.entity.User;
import com.example.HRMS.enums.Approvness;
import com.example.HRMS.repository.ResetPasswordRepository;
import com.example.HRMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResetPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MailService mailService;


    public ResetRequestResponse resetRequest(String email){
        if(!userRepository.existsByEmail(email))
            return new ResetRequestResponse(email, "User doesn't exist in db");

        User user = userRepository.findByEmail(email);

        if(user.getApprovness() == Approvness.NOT_APPROVED)
            return new ResetRequestResponse(user.getEmail(), "User hasn't completed registration");

        String key = UUID.randomUUID().toString();
        ResetPassword item = new ResetPassword(user, key);
        resetPasswordRepository.save(item);
        String message = "Вы получили это электронное письмо, " +
                "потому что вы или кто-то другой запросили " +
                "сброс пароля для вашей учетной записи.\n\n" +
                "Нажмите на ссылку ниже, чтобы сбросить пароль: \n" +
                "http://localhost:8080/reset/set-new-password/key/" + key + "\n" +
                "Если вы не запрашивали сброс пароля, можете смело игнорировать это письмо.";
        mailService.send(user.getEmail(), "Сброс пароля E-mail", message);
        return new ResetRequestResponse(user.getEmail(), "Request is approved");
    }

    public boolean confirmationCheck(String key){
        return resetPasswordRepository.existsByKey(key);
    }


    public MessageResponse setNewPassword(String key, PasswordDto dto){
        if(!confirmationCheck(key))
            return new MessageResponse("Email is not confirmed!");

        if(!resetPasswordRepository.existsByKey(key))
            return new MessageResponse("This link is invalid!");

        ResetPassword res = resetPasswordRepository.findByKey(key);
        User user = res.getUser();
        resetPasswordRepository.delete(res);
        user.setUserPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        return new MessageResponse("Successfully changed!");
    }


}
