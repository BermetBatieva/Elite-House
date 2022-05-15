package com.example.HRMS.repository;

import com.example.HRMS.entity.Department;
import com.example.HRMS.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

//    Department findByIdAndStatus(Long id, Status status);

    boolean existsByNameAndStatus(String name, Status status);

    Optional<Department>findByIdAndStatus(Long Id, Status status);


    List<Department> findByStatus(Status status);
}


