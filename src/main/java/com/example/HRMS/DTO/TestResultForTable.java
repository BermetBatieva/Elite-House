package com.example.HRMS.DTO;


import com.example.HRMS.enums.StatusTest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TestResultForTable {

    private Long id;

    private String name;

    private Double testResult;

    private StatusTest statusTest;
}
