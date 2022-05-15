package com.example.HRMS.repository;

import com.example.HRMS.entity.News;
import com.example.HRMS.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository  extends JpaRepository<News,Long> {

    News findByIdAndStatus(Long id, Status status);
}
