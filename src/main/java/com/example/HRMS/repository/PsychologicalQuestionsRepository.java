package com.example.HRMS.repository;

import com.example.HRMS.entity.PsychologicalQuestions;
import com.example.HRMS.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PsychologicalQuestionsRepository extends JpaRepository<PsychologicalQuestions, Long> {

    boolean existsByQuestion(String question);

    List<PsychologicalQuestions> findAllByStatus(Status status);

    List<PsychologicalQuestions> findByStatusAndPsychologicalType_Id(Status status, Long typeId);
}
