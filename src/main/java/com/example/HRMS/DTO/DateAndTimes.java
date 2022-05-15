package com.example.HRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateAndTimes {
    private String yyyy_mm_dd;
    private Set<TimeAndHrs> timeAndHrs;
}
