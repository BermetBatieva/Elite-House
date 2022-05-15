package com.example.HRMS.services;

import com.example.HRMS.DTO.ApplicationDto;
import com.example.HRMS.DTO.ApplicationHolder;
import com.example.HRMS.entity.User;
import com.example.HRMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName());
    }

    public ResponseEntity<ApplicationHolder> getApplicationStatus(){
        if(getCurrentUser() == null){
            return ResponseEntity.ok().body(new ApplicationHolder(null, "User is not authorised!"));
        }
        User user = getCurrentUser();
        ApplicationDto application = new ApplicationDto();
        application.setPosition(user.getPosition().getName());
        application.setDepartment(user.getPosition().getDepartment().getName());
        application.setApplicationStatus(user.getApplicationStatus());
        application.setAllTestsCompletionDate(user.getAllTestsCompletionDate());
        return ResponseEntity.ok().body(new ApplicationHolder(application, "Success!"));
    }


}
