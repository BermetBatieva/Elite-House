package com.example.HRMS.services;

import com.example.HRMS.DTO.ResponseMessage;
import com.example.HRMS.entity.News;
import com.example.HRMS.entity.NewsImage;
import com.example.HRMS.enums.Status;
import com.example.HRMS.repository.NewsImageRepository;
import com.example.HRMS.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Service
public class NewsImageService {

    @Value("${newsPhoto.path}")
    private String imagePath;
    @Autowired
    private NewsImageRepository newsImageRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Transactional
    public ResponseMessage createNewsImages(Long Id, MultipartFile file) {
        News news = newsRepository.findByIdAndStatus(Id, Status.ACTIVE);
        if(news == null)
            return new ResponseMessage("news is empty!", 400);
        if (file != null) {
                NewsImage newsImage = new NewsImage();
                newsImage.setNews(news);
                newsImage.setStatus(Status.ACTIVE);
                LocalDateTime localDateTime = LocalDateTime.now();
                newsImage.setDateUpload(convertLocalDateTimeToDateUsingInstant(localDateTime));
                newsImage.setImageUrl(set(file));
                newsImageRepository.save(newsImage);
            return new ResponseMessage("successfully added!", 200);
        } else
            return new ResponseMessage("error added!", 400);
    }


    private String set(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String code = UUID.randomUUID().toString();
            Path path = Paths.get(imagePath + code + file.getOriginalFilename().
                    substring(file.getOriginalFilename().lastIndexOf(".")));
            Files.write(path, bytes);
            return path.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    Date convertLocalDateTimeToDateUsingInstant(LocalDateTime dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }


}
