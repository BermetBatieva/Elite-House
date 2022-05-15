package com.example.HRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayInterviewer {
    private String yyyy_mm_dd;
    private Set<TimeInterviewer> times;
}
