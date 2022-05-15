package com.example.HRMS.repository;

import com.example.HRMS.entity.Position;
import com.example.HRMS.entity.Vacancy;
import com.example.HRMS.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    List<Vacancy> findAllByStatus(Status status);

    @Query("select DISTINCT v.position from Vacancy v where v.status = :#{#status}")
    List<Position> findAllGroupByPosition(@Param("status")Status status);

    List<Vacancy> findAllByPosition_Id(Long id);
}
