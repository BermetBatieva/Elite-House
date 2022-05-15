package com.example.HRMS.repository;

import com.example.HRMS.entity.Answer;
import com.example.HRMS.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestion_Id(Long id);
    List<Answer> findAllByQuestion_IdAndStatus(Long id, Status status);
    List<Answer> findAllByQuestion_IdAndCorrectAnswer(Long id, boolean b);
}
