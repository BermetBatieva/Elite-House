package com.example.HRMS.entity;

import com.example.HRMS.enums.ReservationStatus;
import com.example.HRMS.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "interview_time_table")
public class InterviewTimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long id;

    private String startDateTime;

    private String duration;

    private String meetingLink;

    @Column(name="description")
    private String description;

    @ManyToOne
    private User interviewer;

    @ManyToOne
    private Position position;

    @ManyToOne
    private User candidate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String cancelReason;


}
