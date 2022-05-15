package com.example.HRMS.repository;

import com.example.HRMS.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUser_Id(Long userId);

    void deleteAllByUser_Id(Long userId);
}
