package com.example.HRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InfoAboutEmployees {

    private Long id;//это для get all запроса, не трогать!!!

    private String firstname;

    private String lastname;

    private String email;

    private String department;

    private String position;

    private List<TestResultForTable> testResultForTableList;

}
