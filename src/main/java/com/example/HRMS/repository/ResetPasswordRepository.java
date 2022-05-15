package com.example.HRMS.repository;

import com.example.HRMS.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {

    boolean existsByKey(String key);

    @Query("SELECT e FROM ResetPassword e WHERE e.key = :key")
    ResetPassword findByKey(String key);

}
