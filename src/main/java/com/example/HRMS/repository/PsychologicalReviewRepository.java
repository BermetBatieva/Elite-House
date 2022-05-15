package com.example.HRMS.repository;

import com.example.HRMS.entity.PsychologicalReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PsychologicalReviewRepository extends JpaRepository<PsychologicalReview, Long> {

    List<PsychologicalReview> findAllByPsychologicalResults_IdAndAndPsychologicalQuestions_PsychologicalType_Id(Long resultId,Long psychTypeId);

    PsychologicalReview findByPsychologicalResults_Id(Long id);

    List<PsychologicalReview>  findAllByPsychologicalResults_Id(Long resId);


}
