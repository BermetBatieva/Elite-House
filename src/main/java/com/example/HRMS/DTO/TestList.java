package com.example.HRMS.DTO;

import com.example.HRMS.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data

public class TestList {

    private long testId;

    private String nameTest;

    private int quantityQuestions;

    private int numberOfDisplayedQuestions;

    private Status statusTest;

    private boolean status;
}
