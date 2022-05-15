package com.example.HRMS.repository;

import com.example.HRMS.entity.User;
import com.example.HRMS.enums.Approvness;
import com.example.HRMS.enums.Role;
import com.example.HRMS.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByStatusAndRole(Status status, Role role);

    List<User> findAllByStatusAndRoleAndPosition_IdAndPosition_Department_Id(Status status, Role role, Long posId,
                                                                             Long depId);

    List<User> findAllByStatusAndRoleAndPosition_Department_Id(Status status, Role role, Long depId);

    List<User> findByStatusAndRoleAndPosition_IdAndApprovness(Status active, Role candidate, Long posId,
                                                              Approvness approvness);
}
