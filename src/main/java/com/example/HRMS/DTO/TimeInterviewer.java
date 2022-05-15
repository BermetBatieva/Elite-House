package com.example.HRMS.DTO;

import com.example.HRMS.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeInterviewer {
    private String hh_mm;
    private String duration;
    private ReservationStatus reservationStatus;
    private Long candidateId;
    private String candidateFullName;
    private String meetingLink;

}
