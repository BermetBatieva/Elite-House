package com.example.HRMS.DTO;

import com.example.HRMS.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {
    private String position;
    private String department;
    private ApplicationStatus applicationStatus;
    private Date allTestsCompletionDate;
    //private
    //position name
    //department name
    //application status
    //date of application
}
