package com.example.HRMS.DTO;

import com.example.HRMS.enums.StatusTest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class TestsForUsers {

    private Long id;

    private String testName;

    private String remainedTime;

    private StatusTest statusTest;

}
