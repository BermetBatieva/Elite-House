package com.example.HRMS.repository;

import com.example.HRMS.entity.PsychologicalResults;
import com.example.HRMS.enums.StatusTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PsychologicalResultsRepository extends JpaRepository<PsychologicalResults, Long> {


    PsychologicalResults findByUser_Id(Long id);

    PsychologicalResults findByUser_IdAndStatusTest(Long userId, StatusTest statusTest);

}
