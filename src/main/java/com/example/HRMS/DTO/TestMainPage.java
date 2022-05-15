package com.example.HRMS.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data

public class TestMainPage {

    private Long id;
    private String testName;
    private Integer uncompleted;
    private Integer completed;
}
