package com.example.HRMS.repository;

import com.example.HRMS.entity.InterviewTimeTable;
import com.example.HRMS.enums.ReservationStatus;
import com.example.HRMS.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewTimeTableRepository extends JpaRepository<InterviewTimeTable, Long> {
    List<InterviewTimeTable> findAllByStatus(Status status);
    boolean existsByInterviewer_IdAndStartDateTime(Long id, String date);
    List<InterviewTimeTable> findByPosition_IdAndStartDateTimeStartsWithAndReservationStatus(Long id, String s, ReservationStatus reservationStatus);
    List<InterviewTimeTable> findByStartDateTimeStartsWithAndPosition_IdAndReservationStatus(String s, Long id, ReservationStatus reservationStatus);
    List<InterviewTimeTable> findByStartDateTimeAndPosition_IdAndReservationStatus(String fullDate, Long positionId, ReservationStatus reservationStatus);
    List<InterviewTimeTable> findByInterviewer_IdAndStartDateTimeStartsWithAndStatus(Long id, String s, Status status);
    List<InterviewTimeTable> findByStatus(Status status);
    List<InterviewTimeTable> findByCandidate_IdAndReservationStatusAndStatus(Long id, ReservationStatus reservationStatus, Status status);

}
