package com.example.HRMS.services;

import com.example.HRMS.DTO.NewsDto;
import com.example.HRMS.DTO.ResponseMessage;
import com.example.HRMS.entity.News;
import com.example.HRMS.enums.Status;
import com.example.HRMS.repository.NewsImageRepository;
import com.example.HRMS.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public News addNews(NewsDto newsDto) {
        News news = new News();
        news.setDate(convertLocalDateTimeToDateUsingInstant(LocalDateTime.now()));
        news.setStatus(Status.ACTIVE);
        news.setText(newsDto.getText());
        news.setTitle(newsDto.getTitle());
        newsRepository.save(news);
        return news;
    }

    public ResponseMessage deleteNewsById(Long id) {
        News news = newsRepository.findByIdAndStatus(id, Status.ACTIVE);
        if (news == null)
            return new ResponseMessage("news is empty!", 400);
        news.setStatus(Status.REMOVED);
        newsRepository.save(news);

        return new ResponseMessage("News successfully deleted", 200);
    }


    public ResponseMessage updateNewsById(Long id, NewsDto newsDto) {
        News news = newsRepository.findByIdAndStatus(id, Status.ACTIVE);
        if (news == null)
            return new ResponseMessage("news is empty!", 400);
        news.setText(newsDto.getText());
        news.setTitle(newsDto.getTitle());
        newsRepository.save(news);
        return new ResponseMessage("News successfully updated", 200);
    }

    Date convertLocalDateTimeToDateUsingInstant(LocalDateTime dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }


//    public NewsDto getNewsById(Long id){
//        News news = newsRepository.findByIdAndStatus(id,Status.ACTIVE);
//        if(news == null){
//            return new NewsDto("news is empty!");
//        }
//        NewsDto newsDto = new NewsDto();
//        newsDto.setDate(news.getDate());
//        newsDto.setTitle(news.getTitle());
//        newsDto.setText(news.getText());
//        newsDto.setId(news.getId());
//
//       NewsImageService newsImageService = newsImageRepository.
//
//
//    }


}
