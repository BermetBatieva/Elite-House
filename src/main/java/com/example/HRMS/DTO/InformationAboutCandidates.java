package com.example.HRMS.DTO;

import com.example.HRMS.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InformationAboutCandidates {

    private Long id; //это для get all запроса, не трогать!!!

    private String firstname;

    private String lastname;

    private String department;

    private String position;

    private ApplicationStatus applicationStatus;

    private String email;

    private List<TestResultForTable> testResultForTableList;
}
