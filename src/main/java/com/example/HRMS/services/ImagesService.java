package com.example.HRMS.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImagesService {


    @Value("${testPhoto.path}")
    private String photoPath;

    public String set(MultipartFile file, String urlName) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(photoPath + urlName + getFileExtension(file.getOriginalFilename()));
            Files.write(path, bytes);
            return path.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        StringBuilder result = new StringBuilder();
        int index = originalFilename.length() - 1;
        char ch = originalFilename.charAt(index);
        while (ch != '.') {
            result.append(ch);
            index--;
            ch = originalFilename.charAt(index);
        }
        return result.append('.').reverse().toString();
    }


}
