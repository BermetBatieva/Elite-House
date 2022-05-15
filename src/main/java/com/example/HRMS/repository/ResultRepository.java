package com.example.HRMS.repository;

import com.example.HRMS.entity.Result;
import com.example.HRMS.entity.Test;
import com.example.HRMS.enums.Role;
import com.example.HRMS.enums.StatusTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    Result findByUser_Id(Long id);

    List<Result> findAllByUser_Id(Long id);

    List<Result> findAllByUser_IdAndStatusTest(Long id, StatusTest statusTest);

    List<Result> findAllByTest_IdAndStatusTest(Long id, StatusTest statusTest);

    @Query(value = "select r from results r " +
            "where r.test.id = :#{#id} and r.user.role = :#{#role} and " +
            "r.statusTest = 'FINISHED' order by r.attemptDate asc")
    List<Result> Test(@Param("id") Long id, @Param("role") Role role);

    List<Result> findAllByUser_IdOrderByGeneralScoreDesc(Long id);

    @Query(value = "Select avg(r.generalScore) from results r " +
            "where r.statusTest = 'FINISHED' and r.user.role = :#{#role} and r.test.id = :#{#test.id}")
    Double avg(@Param("role") Role role, @Param("test") Test test);


    @Query(value = "Select count(r) from results r " +
            "where (r.statusTest = 'FINISHED' or r.statusTest = 'CHECKED')" +
            "and r.user.role = :#{#role} and r.test.position.id = :#{#positionId}")
    Integer countCompleted(@Param("positionId") Long positionId,
                           @Param("role") Role role);

    @Query(value = "Select count(r) from results r " +
            "where (r.statusTest = 'AVAILABLE' or r.statusTest = 'IP') " +
            "and r.user.role = :#{#role} and r.test.position.id = :#{#positionId}")
    Integer countUncompleted(@Param("positionId") Long positionId,
                             @Param("role") Role role);

    @Query(value = "Select count(r) from results r " +
            "where (r.statusTest = 'FINISHED' or r.statusTest = 'CHECKED') " +
            "and r.user.role = :#{#role} and r.test.id = :#{#testId}")
    Integer countCompletedTest(@Param("testId") Long testId,
                               @Param("role") Role role);

    @Query(value = "Select count(r) from results r " +
            "where (r.statusTest = 'IP' or r.statusTest = 'AVAILABLE') " +
            "and r.user.role = :#{#role} and r.test.id = :#{#testId}")
    Integer countUncompletedTest(@Param("testId") Long testId,
                                 @Param("role") Role role);
}
