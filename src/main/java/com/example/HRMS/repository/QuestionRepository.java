package com.example.HRMS.repository;

import com.example.HRMS.entity.Question;
import com.example.HRMS.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    boolean existsByQuestionAndTest_Id(String question, Long testId);

    List<Question> findAllByTest_IdAndStatus(Long id, Status status);

    List<Question> findAllByTest_Id(Long id);
}
