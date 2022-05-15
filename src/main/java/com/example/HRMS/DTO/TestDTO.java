package com.example.HRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TestDTO {

    private String name;
    private int timeLimit;
    private int numberOfDisplayedQuestions;
    private Long positionId;
}
