package com.example.HRMS.repository;

import com.example.HRMS.entity.PsychologicalType;
import com.example.HRMS.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PsychologicalTypeRepository extends JpaRepository<PsychologicalType, Long> {

    boolean existsByName(String name);



    List<PsychologicalType> findByStatus(Status status);
}
