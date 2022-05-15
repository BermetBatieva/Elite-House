package com.example.HRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeAndHrs {
    private Long slotId;
    private String hh_mm;
    private String duration;
    private String meetingLink;
    private List<Long> interviewerIds;
}
