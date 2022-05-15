package com.example.HRMS.repository;

import com.example.HRMS.entity.NewsImage;
import com.example.HRMS.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsImageRepository extends JpaRepository<NewsImage,Long> {

    List<NewsImage> findByStatusAndNews_Id(Status status, Long id);
}
