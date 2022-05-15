package com.example.HRMS.repository;

import com.example.HRMS.entity.GeneralResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneralResultRepository extends JpaRepository<GeneralResult, Long> {

    List<GeneralResult> findAllByPsychologicalResults_Id(Long id);

    GeneralResult findByPsychologicalType_Id(Long typeId);

    GeneralResult findByPsychologicalResults_IdAndPsychologicalType_Id(Long resultId, Long typeId);


}
