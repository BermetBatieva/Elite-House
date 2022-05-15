package com.example.HRMS.repository;

import com.example.HRMS.entity.Test;
import com.example.HRMS.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {

    boolean existsByName(String name);

    Test findByPosition_Id(Long id);

    boolean existsByNameAndPosition_Id(String name, Long id);


    List<Test> findAllByPosition_IdAndStatus(Long id, Status status);

}
