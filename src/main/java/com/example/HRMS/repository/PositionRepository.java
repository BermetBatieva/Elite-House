package com.example.HRMS.repository;

import com.example.HRMS.entity.Position;
import com.example.HRMS.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findAllByDepartment_IdAndStatus(Long id,Status status);

    Optional<Position> findByIdAndStatus(Long id, Status status);

    boolean existsByNameAndStatusAndDepartment_Id(String name, Status status, Long id);

    List<Position> findByDepartment_Id(Long id);
}
