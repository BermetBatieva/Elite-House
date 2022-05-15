package com.example.HRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyUpcomingInterviews {
    private Long id;
    private Long interviewerId;
    private String interviewerFullName;
    private String date;
    private String time;
    private String meetingLink;
}
