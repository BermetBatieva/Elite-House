package com.example.HRMS.services;

import com.example.HRMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PhotoService {

    @Autowired
    private UserRepository userRepository;


    public void createPhoto(MultipartFile photo) {

    }
}

