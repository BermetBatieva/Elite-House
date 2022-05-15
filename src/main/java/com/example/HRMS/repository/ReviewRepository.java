package com.example.HRMS.repository;

import com.example.HRMS.entity.Review;
import com.example.HRMS.enums.TypeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByResult_Id(Long testId);

    List<Review> findAllByResult_IdAndQuestions_TypeQuestion(Long testId, TypeQuestion typeQuestion);
}
